package sv.edu.ues.reservas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.reservas.domain.entity.Correo;
import sv.edu.ues.reservas.domain.entity.Cuenta;
import sv.edu.ues.reservas.domain.enums.EstadoContacto;
import sv.edu.ues.reservas.dto.correo.CorreoActualizarRequest;
import sv.edu.ues.reservas.dto.correo.CorreoCrearRequest;
import sv.edu.ues.reservas.dto.correo.CorreoResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.CorreoRepository;
import sv.edu.ues.reservas.repository.CuentaRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de CorreoService (JUnit 5 + Mockito, sin PostgreSQL ni contexto Spring).
 *
 * Escenarios aplazados: ninguno. CorreoService solo depende de Cuenta, que ya está estable.
 */

@ExtendWith(MockitoExtension.class)
class CorreoServiceTest {

    @Mock
    private CorreoRepository correoRepository;
    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private CorreoService correoService;

    @Test
    void crearGuardaElCorreoConValoresPredeterminadosYSinEspacios() {
        UUID idCuenta = UUID.randomUUID();
        Cuenta cuenta = cuenta(idCuenta);
        when(cuentaRepository.findById(idCuenta)).thenReturn(Optional.of(cuenta));
        when(correoRepository.save(any(Correo.class))).thenAnswer(inv -> inv.getArgument(0));

        CorreoResponse respuesta = correoService.crear(
                new CorreoCrearRequest(idCuenta, "  ana@ues.edu.sv  ", null, null));

        assertEquals("ana@ues.edu.sv", respuesta.direccionCorreo());
        assertEquals(idCuenta, respuesta.idCuenta());
        assertFalse(respuesta.esPrincipal());
        assertEquals(EstadoContacto.ACTIVO, respuesta.estadoContacto());
        verify(correoRepository, never()).existsByCuenta_IdCuentaAndEsPrincipalTrue(any());
    }

    @Test
    void crearRechazaUnSegundoCorreoPrincipalYNoGuarda() {
        UUID idCuenta = UUID.randomUUID();
        when(cuentaRepository.findById(idCuenta)).thenReturn(Optional.of(cuenta(idCuenta)));
        when(correoRepository.existsByCuenta_IdCuentaAndEsPrincipalTrue(idCuenta)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> correoService.crear(new CorreoCrearRequest(idCuenta, "otro@ues.edu.sv", true, null)));

        assertTrue(ex.getMessage().contains("ya tiene un correo principal"));
        verify(correoRepository, never()).save(any(Correo.class));
    }

    @Test
    void crearLanzaExcepcionSiLaCuentaNoExisteYNoGuarda() {
        UUID idCuenta = UUID.randomUUID();
        when(cuentaRepository.findById(idCuenta)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> correoService.crear(new CorreoCrearRequest(idCuenta, "ana@ues.edu.sv", false, null)));

        verify(correoRepository, never()).save(any(Correo.class));
    }


    @Test
    void obtenerPorIdDevuelveElCorreo() {
        UUID idCorreo = UUID.randomUUID();
        UUID idCuenta = UUID.randomUUID();
        when(correoRepository.findById(idCorreo))
                .thenReturn(Optional.of(correo(idCorreo, cuenta(idCuenta), "ana@ues.edu.sv", false)));

        CorreoResponse respuesta = correoService.obtenerPorId(idCorreo);

        assertEquals(idCorreo, respuesta.idCorreo());
        assertEquals(idCuenta, respuesta.idCuenta());
        assertEquals("ana@ues.edu.sv", respuesta.direccionCorreo());
    }

    @Test
    void obtenerPorIdLanzaExcepcionSiElCorreoNoExiste() {
        UUID idCorreo = UUID.randomUUID();
        when(correoRepository.findById(idCorreo)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> correoService.obtenerPorId(idCorreo));
    }


    @Test
    void actualizarCambiaDireccionYEstadoDeContacto() {
        UUID idCorreo = UUID.randomUUID();
        Correo correo = correo(idCorreo, cuenta(UUID.randomUUID()), "viejo@ues.edu.sv", false);
        when(correoRepository.findById(idCorreo)).thenReturn(Optional.of(correo));
        when(correoRepository.save(any(Correo.class))).thenAnswer(inv -> inv.getArgument(0));

        CorreoResponse respuesta = correoService.actualizar(idCorreo,
                new CorreoActualizarRequest("  nuevo@ues.edu.sv ", null, "inactivo"));

        assertEquals("nuevo@ues.edu.sv", respuesta.direccionCorreo());
        assertEquals(EstadoContacto.INACTIVO, respuesta.estadoContacto());
        assertFalse(respuesta.esPrincipal());
    }

    @Test
    void actualizarRechazaMarcarPrincipalSiYaExisteOtroPrincipalYNoGuarda() {
        UUID idCorreo = UUID.randomUUID();
        UUID idCuenta = UUID.randomUUID();
        Correo correo = correo(idCorreo, cuenta(idCuenta), "ana@ues.edu.sv", false);
        when(correoRepository.findById(idCorreo)).thenReturn(Optional.of(correo));
        when(correoRepository.existsByCuenta_IdCuentaAndEsPrincipalTrueAndIdCorreoNot(idCuenta, idCorreo))
                .thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> correoService.actualizar(idCorreo, new CorreoActualizarRequest(null, true, null)));

        assertTrue(ex.getMessage().contains("otro correo principal"));
        verify(correoRepository, never()).save(any(Correo.class));
    }


    @Test
    void eliminarBorraElCorreoExistente() {
        UUID idCorreo = UUID.randomUUID();
        Correo correo = correo(idCorreo, cuenta(UUID.randomUUID()), "ana@ues.edu.sv", false);
        when(correoRepository.findById(idCorreo)).thenReturn(Optional.of(correo));

        correoService.eliminar(idCorreo);

        ArgumentCaptor<Correo> captor = ArgumentCaptor.forClass(Correo.class);
        verify(correoRepository).delete(captor.capture());
        assertEquals(idCorreo, captor.getValue().getIdCorreo());
    }

    @Test
    void eliminarLanzaExcepcionSiElCorreoNoExiste() {
        UUID idCorreo = UUID.randomUUID();
        when(correoRepository.findById(idCorreo)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> correoService.eliminar(idCorreo));

        verify(correoRepository, never()).delete(any(Correo.class));
    }


    private Cuenta cuenta(UUID id) {
        return Cuenta.builder().idCuenta(id).build();
    }

    private Correo correo(UUID id, Cuenta cuenta, String direccion, boolean principal) {
        return Correo.builder()
                .idCorreo(id)
                .cuenta(cuenta)
                .direccionCorreo(direccion)
                .esPrincipal(principal)
                .estadoContacto(EstadoContacto.ACTIVO)
                .build();
    }
}