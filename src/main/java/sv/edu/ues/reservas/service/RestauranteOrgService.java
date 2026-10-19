package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.RestauranteOrg;
import sv.edu.ues.reservas.dto.restaurante.RestauranteOrgActualizarRequest;
import sv.edu.ues.reservas.dto.restaurante.RestauranteOrgCrearRequest;
import sv.edu.ues.reservas.dto.restaurante.RestauranteOrgResponse;
import sv.edu.ues.reservas.domain.enums.EstadoOrganizacion;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.RestauranteOrgRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestauranteOrgService {

    private static final Logger logger =
            LoggerFactory.getLogger(RestauranteOrgService.class);

    private final RestauranteOrgRepository restauranteOrgRepository;

    @Transactional
    public RestauranteOrgResponse crear(RestauranteOrgCrearRequest request) {

        RestauranteOrg restauranteOrg = new RestauranteOrg();
        restauranteOrg.setNombre(request.nombre().trim());
        restauranteOrg.setEstadoOrganizacion(EstadoOrganizacion.ACTIVA);

        RestauranteOrg guardado = restauranteOrgRepository.save(restauranteOrg);

        logger.info(
                "Organización de restaurantes creada. idRestauranteOrg={}",
                guardado.getIdRestauranteOrg()
        );

        return toResponse(guardado);
    }

    @Transactional
    public RestauranteOrgResponse actualizar(
            UUID id,
            RestauranteOrgActualizarRequest request
    ) {
        RestauranteOrg restauranteOrg = buscarEntidad(id);

        if (request.nombre() != null) {
            restauranteOrg.setNombre(request.nombre().trim());
        }

        RestauranteOrg actualizado =
                restauranteOrgRepository.save(restauranteOrg);

        logger.info(
                "Organización de restaurantes actualizada. idRestauranteOrg={}",
                actualizado.getIdRestauranteOrg()
        );

        return toResponse(actualizado);
    }

    @Transactional
    public void eliminar(UUID id) {
        RestauranteOrg restauranteOrg = buscarEntidad(id);

        restauranteOrgRepository.delete(restauranteOrg);

        logger.info(
                "Organización de restaurantes eliminada. idRestauranteOrg={}",
                restauranteOrg.getIdRestauranteOrg()
        );
    }

    @Transactional
    public RestauranteOrgResponse obtenerPorId(UUID id) {
        return toResponse(buscarEntidad(id));
    }

    @Transactional
    public List<RestauranteOrgResponse> obtenerTodas() {
        return restauranteOrgRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private RestauranteOrg buscarEntidad(UUID id) {
        return restauranteOrgRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Organización de restaurantes no encontrada: " + id
                        )
                );
    }

    private RestauranteOrgResponse toResponse(RestauranteOrg restauranteOrg) {
        return new RestauranteOrgResponse(
                restauranteOrg.getIdRestauranteOrg(),
                restauranteOrg.getNombre(),
                restauranteOrg.getEstadoOrganizacion()
        );
    }
}