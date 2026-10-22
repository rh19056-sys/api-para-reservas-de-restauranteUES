package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.Cuenta;
import sv.edu.ues.reservas.domain.entity.Telefono;
import sv.edu.ues.reservas.domain.enums.EstadoContacto;
import sv.edu.ues.reservas.dto.telefono.TelefonoActualizarRequest;
import sv.edu.ues.reservas.dto.telefono.TelefonoCrearRequest;
import sv.edu.ues.reservas.dto.telefono.TelefonoResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.CuentaRepository;
import sv.edu.ues.reservas.repository.TelefonoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TelefonoService {

    private static final Logger logger = LoggerFactory.getLogger(TelefonoService.class);
    private final TelefonoRepository telefonoRepository;
    private final CuentaRepository cuentaRepository;

    @Transactional
    public TelefonoResponse crear(TelefonoCrearRequest request) {

        Cuenta cuenta = cuentaRepository.findById(request.idCuenta())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Cuenta no encontrada: " + request.idCuenta()
                        )
                );

        String numero = request.numeroTelefono().trim();

        if (telefonoRepository.existsByNumeroTelefono(numero)) {
            throw new IllegalArgumentException(
                    "El número de teléfono ya está registrado"
            );
        }

        boolean principal = Boolean.TRUE.equals(request.esPrincipal());

        if (principal && telefonoRepository
                .existsByCuenta_IdCuentaAndEsPrincipalTrue(
                        request.idCuenta()
                )) {
            throw new IllegalArgumentException(
                    "La cuenta ya tiene un teléfono principal"
            );
        }

        Telefono telefono = Telefono.builder()
                .cuenta(cuenta)
                .numeroTelefono(numero)
                .estadoContacto(parseEstado(request.estadoContacto()))
                .esPrincipal(principal)
                .build();

        Telefono telefonoGuardado = telefonoRepository.save(telefono);

        logger.info(
                "Teléfono creado correctamente. idTelefono={}, idCuenta={}, esPrincipal={}",
                telefonoGuardado.getIdTelefono(),
                telefonoGuardado.getCuenta().getIdCuenta(),
                telefonoGuardado.getEsPrincipal()
        );

        return toResponse(telefonoGuardado);
    }

    @Transactional
    public TelefonoResponse actualizar(
            UUID idTelefono,
            TelefonoActualizarRequest request
    ) {

        Telefono telefono = buscarEntidad(idTelefono);

        String numero = request.numeroTelefono().trim();

        if (!numero.equals(telefono.getNumeroTelefono())
                && telefonoRepository.existsByNumeroTelefono(numero)) {
            throw new IllegalArgumentException(
                    "El número de teléfono ya está registrado"
            );
        }

        telefono.setNumeroTelefono(numero);

        if (request.estadoContacto() != null
                && !request.estadoContacto().isBlank()) {
            telefono.setEstadoContacto(
                    parseEstado(request.estadoContacto())
            );
        }

        if (request.esPrincipal() != null) {

            boolean seraPrincipal = request.esPrincipal();

            if (seraPrincipal && !Boolean.TRUE.equals(telefono.getEsPrincipal())) {

                UUID idCuenta = telefono.getCuenta().getIdCuenta();

                if (telefonoRepository
                        .existsByCuenta_IdCuentaAndEsPrincipalTrueAndIdTelefonoNot(
                                idCuenta,
                                idTelefono
                        )) {
                    throw new IllegalArgumentException(
                            "La cuenta ya tiene otro teléfono principal"
                    );
                }
            }

            telefono.setEsPrincipal(seraPrincipal);
        }

        Telefono telefonoActualizado = telefonoRepository.save(telefono);

        logger.info(
                "Teléfono actualizado correctamente. idTelefono={}, idCuenta={}, esPrincipal={}",
                telefonoActualizado.getIdTelefono(),
                telefonoActualizado.getCuenta().getIdCuenta(),
                telefonoActualizado.getEsPrincipal()
        );

        return toResponse(telefonoActualizado);
    }

    @Transactional
    public void eliminar(UUID idTelefono) {

        Telefono telefono = buscarEntidad(idTelefono);

        telefonoRepository.delete(telefono); //pensé en eliminar primero y luego loggear, pero ¿qué
        //pasa si falla el delete? R// ya estará el mensaje de éxito, antes de la excepción. 

        logger.info(
                "Teléfono eliminado correctamente. idTelefono={}, idCuenta={}, esPrincipal={}",
                telefono.getIdTelefono(),
                telefono.getCuenta().getIdCuenta(),
                telefono.getEsPrincipal()
        );
    }

    @Transactional
    public TelefonoResponse obtenerPorId(UUID idTelefono) {

        return toResponse(buscarEntidad(idTelefono));
    }

    @Transactional
    public List<TelefonoResponse> obtenerTodos() {

        return telefonoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public List<TelefonoResponse> obtenerPorCuenta(UUID idCuenta) {

        if (!cuentaRepository.existsById(idCuenta)) {
            throw new RecursoNoEncontradoException(
                    "Cuenta no encontrada: " + idCuenta
            );
        }

        return telefonoRepository.findByCuenta_IdCuenta(idCuenta)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Telefono buscarEntidad(UUID idTelefono) {

        return telefonoRepository.findById(idTelefono)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Teléfono no encontrado: " + idTelefono
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

    private TelefonoResponse toResponse(Telefono telefono) {

        return new TelefonoResponse(
                telefono.getIdTelefono(),
                telefono.getCuenta().getIdCuenta(),
                telefono.getNumeroTelefono(),
                telefono.getEstadoContacto(),
                telefono.getEsPrincipal(),
                telefono.getFechaCreacion()
        );
    }
}