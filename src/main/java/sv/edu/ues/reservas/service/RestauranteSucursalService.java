package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.RestauranteOrg;
import sv.edu.ues.reservas.domain.entity.RestauranteSucursal;
import sv.edu.ues.reservas.dto.restaurantesucursal.RestauranteSucursalActualizarRequest;
import sv.edu.ues.reservas.dto.restaurantesucursal.RestauranteSucursalCrearRequest;
import sv.edu.ues.reservas.dto.restaurantesucursal.RestauranteSucursalResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.RestauranteOrgRepository;
import sv.edu.ues.reservas.repository.RestauranteSucursalRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestauranteSucursalService {

    private static final Logger logger =
            LoggerFactory.getLogger(RestauranteSucursalService.class);

    private final RestauranteSucursalRepository restauranteSucursalRepository;
    private final RestauranteOrgRepository restauranteOrgRepository;

    @Transactional
    public RestauranteSucursalResponse crear(
            RestauranteSucursalCrearRequest request
    ) {
        RestauranteOrg restauranteOrg = restauranteOrgRepository
                .findById(request.idRestauranteOrg())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Organización de restaurantes no encontrada: "
                                        + request.idRestauranteOrg()
                        )
                );

        RestauranteSucursal sucursal = new RestauranteSucursal();
        sucursal.setRestauranteOrg(restauranteOrg);
        sucursal.setDireccion(request.direccion().trim());
        sucursal.setCapacidad(request.capacidad());
        sucursal.setTipoCocina(request.tipoCocina());

        RestauranteSucursal guardada =
                restauranteSucursalRepository.save(sucursal);

        logger.info(
                "Sucursal creada. idSucursal={}, idRestauranteOrg={}",
                guardada.getIdSucursal(),
                restauranteOrg.getIdRestauranteOrg()
        );

        return toResponse(guardada);
    }

    @Transactional
    public RestauranteSucursalResponse actualizar(
            UUID id,
            RestauranteSucursalActualizarRequest request
    ) {
        RestauranteSucursal sucursal = buscarEntidad(id);

        if (request.direccion() != null) {
            sucursal.setDireccion(request.direccion().trim());
        }

        if (request.capacidad() != null) {
            sucursal.setCapacidad(request.capacidad());
        }

        if (request.tipoCocina() != null) {
            sucursal.setTipoCocina(request.tipoCocina().trim());
        }

        RestauranteSucursal actualizada =
                restauranteSucursalRepository.save(sucursal);

        logger.info(
                "Sucursal actualizada. idSucursal={}",
                actualizada.getIdSucursal()
        );

        return toResponse(actualizada);
    }

    @Transactional
    public void eliminar(UUID id) {
        RestauranteSucursal sucursal = buscarEntidad(id);

        restauranteSucursalRepository.delete(sucursal);

        logger.info(
                "Sucursal eliminada. idSucursal={}",
                sucursal.getIdSucursal()
        );
    }

    @Transactional
    public RestauranteSucursalResponse obtenerPorId(UUID id) {
        return toResponse(buscarEntidad(id));
    }

    @Transactional
    public List<RestauranteSucursalResponse> obtenerTodas() {
        return restauranteSucursalRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public List<RestauranteSucursalResponse> obtenerPorRestauranteOrg(
            UUID idRestauranteOrg
    ) {
        if (!restauranteOrgRepository.existsById(idRestauranteOrg)) {
            throw new RecursoNoEncontradoException(
                    "Organización de restaurantes no encontrada: "
                            + idRestauranteOrg
            );
        }

        return restauranteSucursalRepository
                .findByRestauranteOrg_idRestauranteOrg(idRestauranteOrg)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private RestauranteSucursal buscarEntidad(UUID id) {
        return restauranteSucursalRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Sucursal no encontrada: " + id
                        )
                );
    }

    private RestauranteSucursalResponse toResponse(
            RestauranteSucursal sucursal
    ) {
        return new RestauranteSucursalResponse(
                sucursal.getIdSucursal(),
                sucursal.getRestauranteOrg().getIdRestauranteOrg(),
                sucursal.getDireccion(),
                sucursal.getCapacidad(),
                sucursal.getTipoCocina(),
                sucursal.getEstadoSucursal()
        );
    }
}