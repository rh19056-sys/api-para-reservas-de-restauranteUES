package sv.edu.ues.reservas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sv.edu.ues.reservas.domain.entity.Cuenta;
import sv.edu.ues.reservas.domain.enums.EstadoCuenta;
import sv.edu.ues.reservas.dto.cuenta.CuentaActualizarRequest;
import sv.edu.ues.reservas.dto.cuenta.CuentaCrearRequest;
import sv.edu.ues.reservas.dto.cuenta.CuentaResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.CuentaRepository;

import java.lang.reflect.RecordComponent;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de CuentaService (JUnit 5 + Mockito, sin PostgreSQL ni contexto Spring).
 *
 * Escenarios aplazados: ninguno. CuentaService no depende de relaciones en desarrollo.
 */

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private CuentaService cuentaService;


    @Test
    void crearUsaEstadoPendienteCuandoNoSeProporcionaEstado() {
        UUID idCuenta = UUID.randomUUID();
        when(passwordEncoder.encode("Secreto123")).thenReturn("hash-bcrypt");
        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(inv -> {
            Cuenta c = inv.getArgument(0);
            c.setIdCuenta(idCuenta);
            return c;
        });

        CuentaResponse respuesta = cuentaService.crear(new CuentaCrearRequest("Secreto123", null));

        assertEquals(idCuenta, respuesta.idCuenta());
        assertEquals(EstadoCuenta.PENDIENTE_VERIFICACION, respuesta.estadoCuenta());
    }

    @Test
    void crearCodificaLaContrasenaAntesDeGuardarYNoGuardaElTextoPlano() {
        when(passwordEncoder.encode("Secreto123")).thenReturn("hash-bcrypt");
        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(inv -> inv.getArgument(0));

        cuentaService.crear(new CuentaCrearRequest("Secreto123", "ACTIVA"));

        InOrder orden = inOrder(passwordEncoder, cuentaRepository);
        orden.verify(passwordEncoder).encode("Secreto123");
        ArgumentCaptor<Cuenta> captor = ArgumentCaptor.forClass(Cuenta.class);
        orden.verify(cuentaRepository).save(captor.capture());
        assertEquals("hash-bcrypt", captor.getValue().getPasswordHash());
        assertNotEquals("Secreto123", captor.getValue().getPasswordHash());
    }

    @Test
    void laRespuestaDeCuentaNoExponeElHashDeLaContrasena() {
        List<String> campos = Arrays.stream(CuentaResponse.class.getRecordComponents())
                .map(RecordComponent::getName)
                .toList();

        assertFalse(campos.contains("passwordHash"));
        assertFalse(campos.contains("password"));
    }

    @Test
    void crearRechazaEstadoNoReconocidoYNoGuardaLaCuenta() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cuentaService.crear(new CuentaCrearRequest("Secreto123", "INEXISTENTE")));

        assertTrue(ex.getMessage().contains("Estado de cuenta inválido"));
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }


    @Test
    void obtenerPorIdDevuelveLaCuentaSinExponerDatosSensibles() {
        UUID id = UUID.randomUUID();
        OffsetDateTime creada = OffsetDateTime.parse("2026-10-01T10:00:00-06:00");
        Cuenta cuenta = cuenta(id, "hash", EstadoCuenta.ACTIVA);
        cuenta.setFechaCreacion(creada);
        when(cuentaRepository.findById(id)).thenReturn(Optional.of(cuenta));

        CuentaResponse respuesta = cuentaService.obtenerPorId(id);

        assertEquals(id, respuesta.idCuenta());
        assertEquals(EstadoCuenta.ACTIVA, respuesta.estadoCuenta());
        assertEquals(creada, respuesta.fechaCreacion());
    }

    @Test
    void obtenerPorIdLanzaExcepcionSiLaCuentaNoExiste() {
        UUID id = UUID.randomUUID();
        when(cuentaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> cuentaService.obtenerPorId(id));
    }


    @Test
    void actualizarCambiaEstadoYContrasenaCodificada() {
        UUID id = UUID.randomUUID();
        Cuenta cuenta = cuenta(id, "hash-viejo", EstadoCuenta.PENDIENTE_VERIFICACION);
        when(cuentaRepository.findById(id)).thenReturn(Optional.of(cuenta));
        when(passwordEncoder.encode("NuevaClave1")).thenReturn("hash-nuevo");
        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(inv -> inv.getArgument(0));

        CuentaResponse respuesta = cuentaService.actualizar(id,
                new CuentaActualizarRequest("NuevaClave1", "bloqueada"));

        assertEquals(EstadoCuenta.BLOQUEADA, respuesta.estadoCuenta());
        assertEquals("hash-nuevo", cuenta.getPasswordHash());
        verify(cuentaRepository).save(cuenta);
    }


    @Test
    void eliminarBorraLaCuentaExistente() {
        UUID id = UUID.randomUUID();
        Cuenta cuenta = cuenta(id, "hash", EstadoCuenta.ACTIVA);
        when(cuentaRepository.findById(id)).thenReturn(Optional.of(cuenta));

        cuentaService.eliminar(id);

        verify(cuentaRepository).delete(cuenta);
    }

    @Test
    void eliminarLanzaExcepcionSiLaCuentaNoExiste() {
        UUID id = UUID.randomUUID();
        when(cuentaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> cuentaService.eliminar(id));

        verify(cuentaRepository, never()).delete(any(Cuenta.class));
    }


    private Cuenta cuenta(UUID id, String hash, EstadoCuenta estado) {
        return Cuenta.builder()
                .idCuenta(id)
                .passwordHash(hash)
                .estadoCuenta(estado)
                .build();
    }
}