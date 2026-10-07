package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.Cuenta;
import sv.edu.ues.reservas.domain.enums.EstadoCuenta;
import sv.edu.ues.reservas.dto.cuenta.CuentaActualizarRequest;
import sv.edu.ues.reservas.dto.cuenta.CuentaCrearRequest;
import sv.edu.ues.reservas.dto.cuenta.CuentaResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.CuentaRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public CuentaResponse crear(CuentaCrearRequest request) {

        Cuenta cuenta = Cuenta.builder()
                .passwordHash(passwordEncoder.encode(request.password()))
                .estadoCuenta(parseEstado(request.estadoCuenta()))
                .build();

        return toResponse(cuentaRepository.save(cuenta));
    }

    @Transactional
    public CuentaResponse actualizar(
            UUID id,
            CuentaActualizarRequest request
    ) {

        Cuenta cuenta = buscarEntidad(id);

        if (request.password() != null && !request.password().isBlank()) {
            cuenta.setPasswordHash(
                    passwordEncoder.encode(request.password())
            );
        }

        if (request.estadoCuenta() != null
                && !request.estadoCuenta().isBlank()) {

            cuenta.setEstadoCuenta(
                    parseEstado(request.estadoCuenta())
            );
        }

        return toResponse(cuentaRepository.save(cuenta));
    }

    @Transactional
    public void eliminar(UUID id) {

        Cuenta cuenta = buscarEntidad(id);

        cuentaRepository.delete(cuenta);
    }

    @Transactional
    public CuentaResponse obtenerPorId(UUID id) {

        return toResponse(buscarEntidad(id));
    }

    @Transactional
    public List<CuentaResponse> obtenerTodas() {

        return cuentaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Cuenta buscarEntidad(UUID id) {

        return cuentaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Cuenta no encontrada: " + id
                        )
                );
    }

    private EstadoCuenta parseEstado(String valor) {

        if (valor == null || valor.isBlank()) {
            return EstadoCuenta.PENDIENTE_VERIFICACION;
        }

        try {
            return EstadoCuenta.valueOf(
                    valor.trim().toUpperCase()
            );

        } catch (IllegalArgumentException ex) {

            throw new IllegalArgumentException(
                    "Estado de cuenta inválido: " + valor
            );
        }
    }

    private CuentaResponse toResponse(Cuenta cuenta) {

        return new CuentaResponse(
                cuenta.getIdCuenta(),
                cuenta.getEstadoCuenta(),
                cuenta.getFechaCreacion(),
                cuenta.getUltimoAcceso(),
                cuenta.getFechaVerificacion()
        );
    }
}