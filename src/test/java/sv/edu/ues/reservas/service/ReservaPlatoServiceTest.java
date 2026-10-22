package sv.edu.ues.reservas.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.reservas.domain.entity.Plato;
import sv.edu.ues.reservas.domain.entity.Reserva;
import sv.edu.ues.reservas.domain.entity.ReservaPlato;
import sv.edu.ues.reservas.domain.entity.RestauranteSucursal;
import sv.edu.ues.reservas.domain.enums.EstadoProducto;
import sv.edu.ues.reservas.domain.enums.EstadoReserva;
import sv.edu.ues.reservas.dto.consumo.ConsumoCrearRequest;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.PlatoRepository;
import sv.edu.ues.reservas.repository.ReservaPlatoRepository;
import sv.edu.ues.reservas.repository.ReservaRepository;
import sv.edu.ues.reservas.validation.ReservaValidator;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaPlatoServiceTest {

    @Mock
    private ReservaPlatoRepository reservaPlatoRepository;
    @Mock
    private PlatoRepository platoRepository;
    @Mock
    private ReservaRepository reservaRepository;

    private ReservaPlatoService service;

    private final UUID idSucursal = UUID.randomUUID();
    private final UUID idReserva = UUID.randomUUID();
    private final UUID idPlato = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new ReservaPlatoService(
                reservaPlatoRepository,
                platoRepository,
                new ReservaValidator(reservaRepository)
        );
    }

    @Test
    void agregarTomaPrecioDelCatalogo() {
        stubReserva(EstadoReserva.CONFIRMADA);
        stubPlato(idSucursal, EstadoProducto.ACTIVO);
        when(reservaPlatoRepository.existsById(any())).thenReturn(false);
        when(reservaPlatoRepository.save(any(ReservaPlato.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.agregar(idReserva, new ConsumoCrearRequest(idPlato, 3, null));

        ArgumentCaptor<ReservaPlato> guardado = ArgumentCaptor.forClass(ReservaPlato.class);
        verify(reservaPlatoRepository).save(guardado.capture());
        assertEquals(0, new BigDecimal("8.50").compareTo(guardado.getValue().getPrecioUnitario()));
        assertEquals(idReserva, response.idReserva());
        assertEquals(idPlato, response.idProducto());
        assertEquals(3, response.cantidad());
    }

    @Test
    void agregarRechazaPrecioEnviadoParaPlato() {
        stubReserva(EstadoReserva.PENDIENTE);
        stubPlato(idSucursal, EstadoProducto.ACTIVO);
        when(reservaPlatoRepository.existsById(any())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.agregar(
                idReserva, new ConsumoCrearRequest(idPlato, 1, new BigDecimal("1.00"))));
        verify(reservaPlatoRepository, never()).save(any(ReservaPlato.class));
    }

    @Test
    void agregarRechazaPlatoDeOtraSucursal() {
        stubReserva(EstadoReserva.PENDIENTE);
        stubPlato(UUID.randomUUID(), EstadoProducto.ACTIVO);

        assertThrows(IllegalArgumentException.class,
                () -> service.agregar(idReserva, new ConsumoCrearRequest(idPlato, 1, null)));
        verify(reservaPlatoRepository, never()).save(any(ReservaPlato.class));
    }

    @Test
    void agregarRechazaPlatoInactivo() {
        stubReserva(EstadoReserva.PENDIENTE);
        stubPlato(idSucursal, EstadoProducto.INACTIVO);

        assertThrows(IllegalArgumentException.class,
                () -> service.agregar(idReserva, new ConsumoCrearRequest(idPlato, 1, null)));
    }

    @Test
    void agregarRechazaReservaCancelada() {
        stubReserva(EstadoReserva.CANCELADA);

        assertThrows(IllegalArgumentException.class,
                () -> service.agregar(idReserva, new ConsumoCrearRequest(idPlato, 1, null)));
        verify(platoRepository, never()).findById(any());
    }

    @Test
    void agregarEnReservaInexistenteDevuelveNoEncontrado() {
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.agregar(idReserva, new ConsumoCrearRequest(idPlato, 1, null)));
    }

    private void stubReserva(EstadoReserva estado) {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(idReserva);
        reserva.setSucursal(sucursal(idSucursal));
        reserva.setEstadoReserva(estado);
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));
    }

    private void stubPlato(UUID idSucursalPlato, EstadoProducto estado) {
        Plato plato = new Plato();
        plato.setIdPlato(idPlato);
        plato.setSucursal(sucursal(idSucursalPlato));
        plato.setPrecio(new BigDecimal("8.50"));
        plato.setEstadoProducto(estado);
        when(platoRepository.findById(idPlato)).thenReturn(Optional.of(plato));
    }

    private RestauranteSucursal sucursal(UUID id) {
        RestauranteSucursal sucursal = new RestauranteSucursal();
        sucursal.setIdSucursal(id);
        return sucursal;
    }
}
