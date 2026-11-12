package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import sv.edu.ues.reservas.domain.entity.GestorRestaurante;
import sv.edu.ues.reservas.domain.entity.GestorRestauranteId;
import sv.edu.ues.reservas.domain.entity.PerfilGestor;
import sv.edu.ues.reservas.domain.entity.RestauranteSucursal;
import sv.edu.ues.reservas.domain.enums.EstadoGestor;

import sv.edu.ues.reservas.dto.gestorrestaurante.GestorRestauranteActualizarRequest;
import sv.edu.ues.reservas.dto.gestorrestaurante.GestorRestauranteCrearRequest;
import sv.edu.ues.reservas.dto.gestorrestaurante.GestorRestauranteResponse;

import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;

import sv.edu.ues.reservas.repository.GestorRestauranteRepository;
import sv.edu.ues.reservas.repository.PerfilGestorRepository;
import sv.edu.ues.reservas.repository.RestauranteSucursalRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GestorRestauranteService {

    private static final Logger logger =
            LoggerFactory.getLogger(GestorRestauranteService.class);

    private final GestorRestauranteRepository gestorRestauranteRepository;
    private final PerfilGestorRepository perfilGestorRepository;
    private final RestauranteSucursalRepository restauranteSucursalRepository;

    @Transactional
    public GestorRestauranteResponse crear(
            GestorRestauranteCrearRequest request
    ) {

        UUID idCuenta = request.idCuenta();
        UUID idSucursal = request.idSucursal();

        PerfilGestor perfilGestor = perfilGestorRepository.findById(idCuenta)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Perfil de gestor no encontrado: " + idCuenta
                        )
                );

        RestauranteSucursal sucursal =
                restauranteSucursalRepository.findById(idSucursal)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException(
                                        "Sucursal no encontrada: " + idSucursal
                                )
                        );

        GestorRestauranteId id =
                new GestorRestauranteId(idCuenta, idSucursal);

        if (gestorRestauranteRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "El gestor ya está asignado a esta sucursal"
            );
        }

        GestorRestaurante gestor = new GestorRestaurante();

        gestor.setIdCuenta(perfilGestor.getIdCuenta());
        gestor.setIdSucursal(sucursal.getIdSucursal());
        gestor.setSucursal(sucursal);
        gestor.setEstadoGestor(EstadoGestor.ACTIVO);

        GestorRestaurante guardado =
                gestorRestauranteRepository.save(gestor);

        logger.info(
                "Gestor asignado a sucursal. idCuenta={}, idSucursal={}",
                guardado.getIdCuenta(),
                guardado.getIdSucursal()
        );

        return toResponse(guardado);
    }

    @Transactional
    public List<GestorRestauranteResponse> obtenerTodos() {

        return gestorRestauranteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public GestorRestauranteResponse obtenerPorId(
            UUID idCuenta,
            UUID idSucursal
    ) {

        return toResponse(buscarEntidad(idCuenta, idSucursal));
    }

    @Transactional
    public List<GestorRestauranteResponse> obtenerPorSucursal(
            UUID idSucursal
    ) {

        if (!restauranteSucursalRepository.existsById(idSucursal)) {
            throw new RecursoNoEncontradoException(
                    "Sucursal no encontrada: " + idSucursal
            );
        }

        return gestorRestauranteRepository.findByIdSucursal(idSucursal)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public List<GestorRestauranteResponse> obtenerPorCuenta(
            UUID idCuenta
    ) {

        if (!perfilGestorRepository.existsById(idCuenta)) {
            throw new RecursoNoEncontradoException(
                    "Perfil de gestor no encontrado: " + idCuenta
            );
        }

        return gestorRestauranteRepository.findByIdCuenta(idCuenta)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public GestorRestauranteResponse actualizar(
            UUID idCuenta,
            UUID idSucursal,
            GestorRestauranteActualizarRequest request
    ) {

        GestorRestaurante gestor = buscarEntidad(idCuenta, idSucursal);

        gestor.setEstadoGestor(request.estadoGestor());

        GestorRestaurante actualizado =
                gestorRestauranteRepository.save(gestor);

        logger.info(
                "Asignación de gestor actualizada. idCuenta={}, idSucursal={}",
                idCuenta,
                idSucursal
        );

        return toResponse(actualizado);
    }

    @Transactional
    public void eliminar(UUID idCuenta, UUID idSucursal) {

        GestorRestaurante gestor = buscarEntidad(idCuenta, idSucursal);

        gestorRestauranteRepository.delete(gestor);

        logger.info(
                "Asignación de gestor eliminada. idCuenta={}, idSucursal={}",
                idCuenta,
                idSucursal
        );
    }

    private GestorRestaurante buscarEntidad(
            UUID idCuenta,
            UUID idSucursal
    ) {

        GestorRestauranteId id =
                new GestorRestauranteId(idCuenta, idSucursal);

        return gestorRestauranteRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Asignación de gestor no encontrada"
                        )
                );
    }

    private GestorRestauranteResponse toResponse(
            GestorRestaurante gestor
    ) {

        PerfilGestor perfilGestor = perfilGestorRepository
                .findById(gestor.getIdCuenta())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Perfil de gestor no encontrado: "
                                        + gestor.getIdCuenta()
                        )
                );

        return new GestorRestauranteResponse(
                gestor.getIdCuenta(),
                gestor.getIdSucursal(),
                perfilGestor.getNombre(),
                gestor.getEstadoGestor()
        );
    }
}