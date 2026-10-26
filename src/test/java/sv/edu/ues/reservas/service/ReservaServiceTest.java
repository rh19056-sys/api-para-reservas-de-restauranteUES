package sv.edu.ues.reservas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.reservas.domain.entity.PerfilCliente;
import sv.edu.ues.reservas.domain.entity.Reserva;
import sv.edu.ues.reservas.domain.entity.RestauranteSucursal;
import sv.edu.ues.reservas.domain.enums.EstadoReserva;
import sv.edu.ues.reservas.dto.reserva.ReservaActualizarRequest;
import sv.edu.ues.reservas.dto.reserva.ReservaCrearRequest;
import sv.edu.ues.reservas.dto.reserva.ReservaResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.EventoRepository;
import sv.edu.ues.reservas.repository.MesaRepository;
import sv.edu.ues.reservas.repository.PerfilClienteRepository;
import sv.edu.ues.reservas.repository.PoliticaReservaRepository;
import sv.edu.ues.reservas.repository.ReservaMesaRepository;
import sv.edu.ues.reservas.repository.ReservaRepository;
import sv.edu.ues.reservas.repository.RestauranteSucursalRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de ReservaService (JUnit 5 + Mockito, sin PostgreSQL ni contexto Spring).
 *
 * Escenarios aplazados (según el issue #35, relaciones aún en desarrollo):
 *  - creación y actualización exitosas de una reserva, porque requieren la asignación de mesas;
 *  - reemplazo de mesas de una reserva;
 *  - conflictos de horario entre reservas de una misma mesa;
 *  - validación de capacidad total de las mesas;
 *  - coherencia de la política y del evento con la sucursal de la reserva.
 * Estas pruebas cubren consultas, errores de recurso inexistente y las validaciones
 * independientes de horas, número de comensales y mesas duplicadas.
 */

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    private static final OffsetDateTime INICIO = OffsetDateTime.parse("2026-11-01T19:00:00-06:00");
    private static final OffsetDateTime FIN = OffsetDateTime.parse("2026-11-01T21:00:00-06:00");

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private ReservaMesaRepository reservaMesaRepository;
    @Mock
    private PerfilClienteRepository perfilClienteRepository;
    @Mock
    private RestauranteSucursalRepository sucursalRepository;
    @Mock
    private PoliticaReservaRepository politicaRepository;
    @Mock
    private EventoRepository eventoRepository;
    @Mock
    private MesaRepository mesaRepository;

    @InjectMocks
    private ReservaService reservaService;


    @Test
    void obtenerPorIdDevuelveLaReservaConSusDatosBasicos() {
        UUID idReserva = UUID.randomUUID();
        UUID idCliente = UUID.randomUUID();
        UUID idSucursal = UUID.randomUUID();
        Reserva reserva = reserva(idReserva, cliente(idCliente), sucursal(idSucursal), EstadoReserva.CONFIRMADA);
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));

        ReservaResponse respuesta = reservaService.obtenerPorId(idReserva);

        assertEquals(idReserva, respuesta.idReserva());
        assertEquals(idCliente, respuesta.idCuentaCliente());
        assertEquals(idSucursal, respuesta.idSucursal());
        assertEquals(4, respuesta.numeroComensales());
        assertEquals(INICIO, respuesta.horaInicio());
        assertEquals(FIN, respuesta.horaFin());
        assertEquals(EstadoReserva.CONFIRMADA, respuesta.estadoReserva());
        assertNull(respuesta.idPolitica());
        assertNull(respuesta.idEvento());
        assertTrue(respuesta.idsMesas().isEmpty());
    }

    @Test
    void obtenerPorIdLanzaExcepcionSiLaReservaNoExiste() {
        UUID idReserva = UUID.randomUUID();
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> reservaService.obtenerPorId(idReserva));
    }

    @Test
    void obtenerPorClienteDevuelveSoloLasReservasDeEseCliente() {
        UUID idCliente = UUID.randomUUID();
        PerfilCliente cliente = cliente(idCliente);
        when(perfilClienteRepository.existsById(idCliente)).thenReturn(true);
        when(reservaRepository.findByPerfilCliente_IdCuenta(idCliente)).thenReturn(List.of(
                reserva(UUID.randomUUID(), cliente, sucursal(UUID.randomUUID()), EstadoReserva.PENDIENTE)));

        List<ReservaResponse> respuestas = reservaService.obtenerPorCliente(idCliente);

        assertEquals(1, respuestas.size());
        assertEquals(idCliente, respuestas.get(0).idCuentaCliente());
    }


    @Test
    void crearLanzaExcepcionSiElClienteNoExisteYNoGuarda() {
        UUID idCliente = UUID.randomUUID();
        when(perfilClienteRepository.findById(idCliente)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> reservaService.crear(solicitud(idCliente, UUID.randomUUID(), 4, INICIO, FIN,
                        List.of(UUID.randomUUID()))));

        verify(reservaRepository, never()).save(any(Reserva.class));
    }


    @Test
    void crearRechazaHoraDeFinAnteriorALaDeInicioYNoGuarda() {
        UUID idCliente = UUID.randomUUID();
        UUID idSucursal = UUID.randomUUID();
        prepararClienteYSucursal(idCliente, idSucursal);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> reservaService.crear(solicitud(idCliente, idSucursal, 4, FIN, INICIO,
                        List.of(UUID.randomUUID()))));

        assertTrue(ex.getMessage().contains("hora de fin"));
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void crearRechazaNumeroDeComensalesMenorQueUnoYNoGuarda() {
        UUID idCliente = UUID.randomUUID();
        UUID idSucursal = UUID.randomUUID();
        prepararClienteYSucursal(idCliente, idSucursal);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> reservaService.crear(solicitud(idCliente, idSucursal, 0, INICIO, FIN,
                        List.of(UUID.randomUUID()))));

        assertTrue(ex.getMessage().contains("comensales"));
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void crearRechazaMesasDuplicadasYNoGuarda() {
        UUID idCliente = UUID.randomUUID();
        UUID idSucursal = UUID.randomUUID();
        UUID idMesa = UUID.randomUUID();
        prepararClienteYSucursal(idCliente, idSucursal);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> reservaService.crear(solicitud(idCliente, idSucursal, 4, INICIO, FIN,
                        List.of(idMesa, idMesa))));

        assertTrue(ex.getMessage().contains("duplicadas"));
        verify(reservaRepository, never()).save(any(Reserva.class));
        verify(reservaMesaRepository, never()).saveAll(anyList());
    }


    @Test
    void actualizarLanzaExcepcionSiLaReservaNoExisteYNoGuarda() {
        UUID idReserva = UUID.randomUUID();
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> reservaService.actualizar(idReserva, cambio(null, null, null, EstadoReserva.CONFIRMADA)));

        verify(reservaRepository, never()).save(any(Reserva.class));
    }


    @Test
    void eliminarBorraLasMesasAsignadasYLaReserva() {
        UUID idReserva = UUID.randomUUID();
        Reserva reserva = reserva(idReserva, cliente(UUID.randomUUID()), sucursal(UUID.randomUUID()),
                EstadoReserva.CANCELADA);
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));

        reservaService.eliminar(idReserva);

        verify(reservaMesaRepository).deleteAll(anyList());
        verify(reservaRepository).delete(reserva);
    }

    @Test
    void eliminarLanzaExcepcionSiLaReservaNoExiste() {
        UUID idReserva = UUID.randomUUID();
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> reservaService.eliminar(idReserva));

        verify(reservaRepository, never()).delete(any(Reserva.class));
    }


    private void prepararClienteYSucursal(UUID idCliente, UUID idSucursal) {
        when(perfilClienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente(idCliente)));
        when(sucursalRepository.findById(idSucursal)).thenReturn(Optional.of(sucursal(idSucursal)));
    }

    private ReservaCrearRequest solicitud(UUID idCliente, UUID idSucursal, Integer comensales,
                                          OffsetDateTime inicio, OffsetDateTime fin, List<UUID> idsMesas) {
        return new ReservaCrearRequest(idCliente, idSucursal, null, null, comensales,
                inicio, fin, null, null, idsMesas);
    }

    private ReservaActualizarRequest cambio(Integer comensales, OffsetDateTime horaFin,
                                            List<UUID> idsMesas, EstadoReserva estado) {
        return new ReservaActualizarRequest(null, null, null, null, comensales,
                null, horaFin, estado, null, idsMesas);
    }

    private PerfilCliente cliente(UUID idCuenta) {
        return PerfilCliente.builder().idCuenta(idCuenta).build();
    }

    private RestauranteSucursal sucursal(UUID id) {
        RestauranteSucursal sucursal = new RestauranteSucursal();
        sucursal.setIdSucursal(id);
        return sucursal;
    }

    private Reserva reserva(UUID id, PerfilCliente cliente, RestauranteSucursal sucursal, EstadoReserva estado) {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(id);
        reserva.setPerfilCliente(cliente);
        reserva.setSucursal(sucursal);
        reserva.setNumeroComensales(4);
        reserva.setHoraInicio(INICIO);
        reserva.setHoraFin(FIN);
        reserva.setEstadoReserva(estado);
        return reserva;
    }
}