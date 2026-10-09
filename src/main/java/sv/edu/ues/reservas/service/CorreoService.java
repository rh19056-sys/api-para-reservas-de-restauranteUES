package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.Correo;
import sv.edu.ues.reservas.domain.entity.Cuenta;
import sv.edu.ues.reservas.domain.enums.EstadoContacto;
import sv.edu.ues.reservas.dto.correo.CorreoActualizarRequest;
import sv.edu.ues.reservas.dto.correo.CorreoCrearRequest;
import sv.edu.ues.reservas.dto.correo.CorreoResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.CorreoRepository;
import sv.edu.ues.reservas.repository.CuentaRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CorreoService {

    private final CorreoRepository correoRepository;
    private final CuentaRepository cuentaRepository;

    @Transactional
    public CorreoResponse crear(CorreoCrearRequest request) {

        Cuenta cuenta = cuentaRepository.findById(request.idCuenta())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Cuenta no encontrada: " + request.idCuenta()
                        )
                );

        boolean principal = Boolean.TRUE.equals(request.esPrincipal());

        if (principal && correoRepository
                .existsByCuenta_IdCuentaAndEsPrincipalTrue(
                        request.idCuenta()
                )) {
            throw new IllegalArgumentException(
                    "La cuenta ya tiene un correo principal"
            );
        }

        Correo correo = Correo.builder()
                .cuenta(cuenta)
                .direccionCorreo(request.direccionCorreo().trim())
                .esPrincipal(principal)
                .estadoContacto(parseEstado(request.estadoContacto()))
                .build();

        return toResponse(correoRepository.save(correo));
    }

    @Transactional
    public CorreoResponse actualizar(
            UUID idCorreo,
            CorreoActualizarRequest request
    ) {

        Correo correo = buscarEntidad(idCorreo);

        if (request.direccionCorreo() != null) {
            correo.setDireccionCorreo(
                    request.direccionCorreo().trim()
            );
        }

        if (request.estadoContacto() != null
                && !request.estadoContacto().isBlank()) {
            correo.setEstadoContacto(
                    parseEstado(request.estadoContacto())
            );
        }

        if (request.esPrincipal() != null) {

            boolean seraPrincipal = request.esPrincipal();

            if (seraPrincipal && !Boolean.TRUE.equals(correo.getEsPrincipal())) {

                UUID idCuenta = correo.getCuenta().getIdCuenta();

                if (correoRepository
                        .existsByCuenta_IdCuentaAndEsPrincipalTrueAndIdCorreoNot(
                                idCuenta,
                                idCorreo
                        )) {
                    throw new IllegalArgumentException(
                            "La cuenta ya tiene otro correo principal"
                    );
                }
            }

            correo.setEsPrincipal(seraPrincipal);
        }

        return toResponse(correoRepository.save(correo));
    }

    @Transactional
    public void eliminar(UUID idCorreo) {

        Correo correo = buscarEntidad(idCorreo);

        correoRepository.delete(correo);
    }

    @Transactional
    public CorreoResponse obtenerPorId(UUID idCorreo) {

        return toResponse(buscarEntidad(idCorreo));
    }

    @Transactional
    public List<CorreoResponse> obtenerTodos() {

        return correoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public List<CorreoResponse> obtenerPorCuenta(UUID idCuenta) {

        if (!cuentaRepository.existsById(idCuenta)) {
            throw new RecursoNoEncontradoException(
                    "Cuenta no encontrada: " + idCuenta
            );
        }

        return correoRepository.findByCuenta_IdCuenta(idCuenta)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Correo buscarEntidad(UUID idCorreo) {

        return correoRepository.findById(idCorreo)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Correo no encontrado: " + idCorreo
                        )
                );
    }

    private EstadoContacto parseEstado(String valor) {

        if (valor == null || valor.isBlank()) {
            return EstadoContacto.ACTIVO;
        }

        try {
            return EstadoContacto.valueOf(
                    valor.trim().toUpperCase()
            );
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Estado de contacto inválido: " + valor
            );
        }
    }

    private CorreoResponse toResponse(Correo correo) {

        return new CorreoResponse(
                correo.getIdCorreo(),
                correo.getCuenta().getIdCuenta(),
                correo.getDireccionCorreo(),
                correo.getEstadoContacto(),
                correo.getEsPrincipal(),
                correo.getFechaCreacion()
        );
    }
}