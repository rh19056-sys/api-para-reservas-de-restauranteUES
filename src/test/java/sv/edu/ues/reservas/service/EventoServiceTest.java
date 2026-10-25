package sv.edu.ues.reservas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.reservas.domain.entity.Evento;
import sv.edu.ues.reservas.domain.entity.RestauranteSucursal;
import sv.edu.ues.reservas.domain.enums.EstadoEvento;
import sv.edu.ues.reservas.dto.evento.EventoActualizarRequest;
import sv.edu.ues.reservas.dto.evento.EventoCrearRequest;
import sv.edu.ues.reservas.dto.evento.EventoResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.BebidaRepository;
import sv.edu.ues.reservas.repository.EventoBebidaRepository;
import sv.edu.ues.reservas.repository.EventoMenuRepository;
import sv.edu.ues.reservas.repository.EventoPlatoRepository;
import sv.edu.ues.reservas.repository.EventoRepository;
import sv.edu.ues.reservas.repository.MenuRepository;
import sv.edu.ues.reservas.repository.PlatoRepository;
import sv.edu.ues.reservas.repository.RestauranteSucursalRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de EventoService (JUnit 5 + Mockito, sin PostgreSQL ni contexto Spring).
 *
 * Escenarios aplazados (según el issue #37, relaciones aún en desarrollo):
 *  - asignación y reemplazo de menús, platos y bebidas de un evento;
 *  - validación de que esos productos pertenezcan a la misma sucursal;
 *  - rechazo de productos duplicados dentro de un evento.
 * Estas pruebas se limitan al comportamiento básico estable del evento.
 */

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    private static final OffsetDateTime FECHA = OffsetDateTime.parse("2026-12-24T19:00:00-06:00");

    @Mock
    private EventoRepository eventoRepository;
    @Mock
    private EventoMenuRepository eventoMenuRepository;
    @Mock
    private EventoPlatoRepository eventoPlatoRepository;
    @Mock
    private EventoBebidaRepository eventoBebidaRepository;
    @Mock
    private RestauranteSucursalRepository sucursalRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private PlatoRepository platoRepository;
    @Mock
    private BebidaRepository bebidaRepository;

    @InjectMocks
    private EventoService eventoService;


    @Test
    void crearGuardaUnEventoBasicoSinProductosAsociados() {
        UUID idSucursal = UUID.randomUUID();
        UUID idEvento = UUID.randomUUID();
        when(sucursalRepository.findById(idSucursal)).thenReturn(Optional.of(sucursal(idSucursal)));
        when(eventoRepository.save(any(Evento.class))).thenAnswer(inv -> {
            Evento e = inv.getArgument(0);
            e.setIdEvento(idEvento);
            return e;
        });

        EventoResponse respuesta = eventoService.crear(new EventoCrearRequest(
                idSucursal, "  Cena de gala  ", "  Corporativo ", FECHA, EstadoEvento.PROGRAMADO,
                null, null, null));

        assertEquals(idEvento, respuesta.idEvento());
        assertEquals(idSucursal, respuesta.idSucursal());
        assertEquals("Cena de gala", respuesta.titulo());
        assertEquals("Corporativo", respuesta.tipo());
        assertEquals(FECHA, respuesta.fechaEvento());
        assertEquals(EstadoEvento.PROGRAMADO, respuesta.estadoEvento());
        assertTrue(respuesta.menus().isEmpty());
        assertTrue(respuesta.platos().isEmpty());
        assertTrue(respuesta.bebidas().isEmpty());
        verify(eventoMenuRepository, never()).saveAll(anyList());
        verify(eventoPlatoRepository, never()).saveAll(anyList());
        verify(eventoBebidaRepository, never()).saveAll(anyList());
    }

    @Test
    void crearLanzaExcepcionSiLaSucursalNoExisteYNoGuardaElEvento() {
        UUID idSucursal = UUID.randomUUID();
        when(sucursalRepository.findById(idSucursal)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> eventoService.crear(new EventoCrearRequest(
                        idSucursal, "Cena", "Social", FECHA, EstadoEvento.PROGRAMADO, null, null, null)));

        verify(eventoRepository, never()).save(any(Evento.class));
    }


    @Test
    void obtenerPorIdDevuelveElEvento() {
        UUID idSucursal = UUID.randomUUID();
        Evento evento = evento(UUID.randomUUID(), sucursal(idSucursal), "Boda", "Social", EstadoEvento.ACTIVO);
        when(eventoRepository.findById(evento.getIdEvento())).thenReturn(Optional.of(evento));

        EventoResponse respuesta = eventoService.obtenerPorId(evento.getIdEvento());

        assertEquals(evento.getIdEvento(), respuesta.idEvento());
        assertEquals(idSucursal, respuesta.idSucursal());
        assertEquals("Boda", respuesta.titulo());
        assertEquals(EstadoEvento.ACTIVO, respuesta.estadoEvento());
    }

    @Test
    void obtenerPorIdLanzaExcepcionSiElEventoNoExiste() {
        UUID id = UUID.randomUUID();
        when(eventoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> eventoService.obtenerPorId(id));
    }


    @Test
    void actualizarCambiaLosCamposBasicosSinTocarLosProductos() {
        UUID id = UUID.randomUUID();
        Evento evento = evento(id, sucursal(UUID.randomUUID()), "Boda", "Social", EstadoEvento.PROGRAMADO);
        OffsetDateTime nuevaFecha = FECHA.plusDays(7);
        when(eventoRepository.findById(id)).thenReturn(Optional.of(evento));
        when(eventoRepository.save(any(Evento.class))).thenAnswer(inv -> inv.getArgument(0));

        EventoResponse respuesta = eventoService.actualizar(id, new EventoActualizarRequest(
                null, "  Boda de plata ", " Aniversario ", nuevaFecha, EstadoEvento.FINALIZADO,
                null, null, null));

        assertEquals("Boda de plata", respuesta.titulo());
        assertEquals("Aniversario", respuesta.tipo());
        assertEquals(nuevaFecha, respuesta.fechaEvento());
        assertEquals(EstadoEvento.FINALIZADO, respuesta.estadoEvento());
        verify(eventoMenuRepository, never()).deleteAll(anyList());
        verify(eventoPlatoRepository, never()).deleteAll(anyList());
        verify(eventoBebidaRepository, never()).deleteAll(anyList());
    }

    @Test
    void actualizarLanzaExcepcionSiElEventoNoExiste() {
        UUID id = UUID.randomUUID();
        when(eventoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> eventoService.actualizar(id, new EventoActualizarRequest(
                        null, "Nuevo", null, null, null, null, null, null)));

        verify(eventoRepository, never()).save(any(Evento.class));
    }


    @Test
    void eliminarBorraPrimeroLasRelacionesYLuegoElEvento() {
        UUID id = UUID.randomUUID();
        Evento evento = evento(id, sucursal(UUID.randomUUID()), "Boda", "Social", EstadoEvento.CANCELADO);
        when(eventoRepository.findById(id)).thenReturn(Optional.of(evento));

        eventoService.eliminar(id);

        InOrder orden = inOrder(eventoMenuRepository, eventoPlatoRepository,
                eventoBebidaRepository, eventoRepository);
        orden.verify(eventoMenuRepository).deleteAll(anyList());
        orden.verify(eventoPlatoRepository).deleteAll(anyList());
        orden.verify(eventoBebidaRepository).deleteAll(anyList());
        ArgumentCaptor<Evento> captor = ArgumentCaptor.forClass(Evento.class);
        orden.verify(eventoRepository).delete(captor.capture());
        assertEquals(id, captor.getValue().getIdEvento());
    }

    @Test
    void eliminarLanzaExcepcionSiElEventoNoExiste() {
        UUID id = UUID.randomUUID();
        when(eventoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> eventoService.eliminar(id));

        verify(eventoRepository, never()).delete(any(Evento.class));
    }


    private RestauranteSucursal sucursal(UUID id) {
        RestauranteSucursal sucursal = new RestauranteSucursal();
        sucursal.setIdSucursal(id);
        return sucursal;
    }

    private Evento evento(UUID id, RestauranteSucursal sucursal, String titulo, String tipo, EstadoEvento estado) {
        Evento evento = new Evento();
        evento.setIdEvento(id);
        evento.setSucursal(sucursal);
        evento.setTitulo(titulo);
        evento.setTipo(tipo);
        evento.setFechaEvento(FECHA);
        evento.setEstadoEvento(estado);
        return evento;
    }
}