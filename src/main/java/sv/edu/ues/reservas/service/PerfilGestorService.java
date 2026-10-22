package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.Cuenta;
import sv.edu.ues.reservas.domain.entity.PerfilGestor;
import sv.edu.ues.reservas.dto.perfilgestor.PerfilGestorActualizarRequest;
import sv.edu.ues.reservas.dto.perfilgestor.PerfilGestorCrearRequest;
import sv.edu.ues.reservas.dto.perfilgestor.PerfilGestorResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.CuentaRepository;
import sv.edu.ues.reservas.repository.PerfilGestorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerfilGestorService {

    private static final Logger logger =
            LoggerFactory.getLogger(PerfilGestorService.class);
    private final PerfilGestorRepository perfilGestorRepository;
    private final CuentaRepository cuentaRepository;

    @Transactional
    public PerfilGestorResponse crear(
            UUID idCuenta,
            PerfilGestorCrearRequest request
    ) {

        Cuenta cuenta = cuentaRepository.findById(idCuenta)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Cuenta no encontrada: " + idCuenta
                        )
                );

        if (perfilGestorRepository.existsById(idCuenta)) {
            throw new IllegalArgumentException(
                    "La cuenta ya posee un perfil de gestor"
            );
        }

        PerfilGestor perfilGestor = PerfilGestor.builder()
                .cuenta(cuenta)
                .nombre(request.nombre().trim())
                .build();

        PerfilGestor guardado =
                perfilGestorRepository.save(perfilGestor);

        logger.info(
                "Se creó el Perfil de gestor. idCuenta={}",
                guardado.getIdCuenta()
        );

        return toResponse(guardado);
    }

    @Transactional
    public List<PerfilGestorResponse> obtenerTodos() {

        return perfilGestorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PerfilGestorResponse obtenerPorId(UUID idCuenta) {

        return toResponse(buscarEntidad(idCuenta));
    }

    @Transactional
    public PerfilGestorResponse actualizar(
            UUID idCuenta,
            PerfilGestorActualizarRequest request
    ) {

        PerfilGestor perfilGestor = buscarEntidad(idCuenta);

        if (request.nombre() != null
                && !request.nombre().isBlank()) {

            perfilGestor.setNombre(request.nombre().trim());
        }

        PerfilGestor actualizado =
                perfilGestorRepository.save(perfilGestor);
        logger.info(
        "Perfil de gestor actualizado correctamente. idCuenta={}",
        actualizado.getIdCuenta()
        );
        return toResponse(actualizado);
    }

    @Transactional
    public void eliminar(UUID idCuenta) {

        PerfilGestor perfilGestor = buscarEntidad(idCuenta);

        logger.info(
                "Perfil de gestor eliminado correctamente. idCuenta={}",
                perfilGestor.getIdCuenta()
        );

        perfilGestorRepository.delete(perfilGestor);
    }

    private PerfilGestor buscarEntidad(UUID idCuenta) {

        return perfilGestorRepository.findById(idCuenta)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Perfil de gestor no encontrado: "
                                        + idCuenta
                        )
                );
    }

    private PerfilGestorResponse toResponse(
            PerfilGestor perfilGestor
    ) {

        return new PerfilGestorResponse(
                perfilGestor.getIdCuenta(),
                perfilGestor.getNombre()
        );
    }
}