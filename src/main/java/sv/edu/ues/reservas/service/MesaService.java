package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import sv.edu.ues.reservas.domain.entity.Mesa;
import sv.edu.ues.reservas.domain.entity.RestauranteSucursal;
import sv.edu.ues.reservas.domain.enums.EstadoMesa;

import sv.edu.ues.reservas.dto.mesa.MesaCrearRequest;
import sv.edu.ues.reservas.dto.mesa.MesaActualizarRequest;
import sv.edu.ues.reservas.dto.mesa.MesaResponse;

import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;

import sv.edu.ues.reservas.repository.MesaRepository;
import sv.edu.ues.reservas.repository.RestauranteSucursalRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MesaService {

    private static final Logger logger =
            LoggerFactory.getLogger(MesaService.class);

    private final MesaRepository mesaRepository;
    private final RestauranteSucursalRepository restauranteSucursalRepository;

    // CREAR MESA
    @Transactional
    public MesaResponse crear(MesaCrearRequest request) {

        RestauranteSucursal sucursal = restauranteSucursalRepository
                .findById(request.idSucursal())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Sucursal no encontrada: " + request.idSucursal()
                        )
                );

        validarNumeroDisponible(
                request.idSucursal(),
                request.numero(),
                null
        );

        Mesa mesa = new Mesa();
        mesa.setSucursal(sucursal);
        mesa.setNumero(request.numero());
        mesa.setCapacidad(request.capacidad());
        mesa.setUbicacion(request.ubicacion().trim());
        mesa.setEstadoMesa(EstadoMesa.DISPONIBLE);

        Mesa guardada = mesaRepository.save(mesa);

        logger.info(
                "Mesa creada. idMesa={}, idSucursal={}, numero={}",
                guardada.getIdMesa(),
                request.idSucursal(),
                guardada.getNumero()
        );

        return toResponse(guardada);
    }

    // OBTENER TODAS LAS MESAS
    @Transactional
    public List<MesaResponse> obtenerTodas() {
        return mesaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // OBTENER MESA POR ID
    @Transactional
    public MesaResponse obtenerPorId(UUID id) {
        return toResponse(buscarEntidad(id));
    }

    // OBTENER MESAS POR SUCURSAL
    @Transactional
    public List<MesaResponse> obtenerPorSucursal(@NonNull     UUID idSucursal) {

        if (!restauranteSucursalRepository.existsById(idSucursal)) {
            throw new RecursoNoEncontradoException(
                    "Sucursal no encontrada: " + idSucursal
            );
        }

        return mesaRepository.findBySucursal_IdSucursal(idSucursal)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ACTUALIZAR MESA
    @Transactional
    public MesaResponse actualizar(
            UUID id,
            MesaActualizarRequest request
    ) {

        Mesa mesa = buscarEntidad(id);

        Integer nuevoNumero = request.numero() != null
                ? request.numero()
                : mesa.getNumero();

        validarNumeroDisponible(
                mesa.getSucursal().getIdSucursal(),
                nuevoNumero,
                id
        );

        if (request.numero() != null) {
            mesa.setNumero(request.numero());
        }

        if (request.capacidad() != null) {
            mesa.setCapacidad(request.capacidad());
        }

        if (request.ubicacion() != null) {
            mesa.setUbicacion(request.ubicacion().trim());
        }

        if (request.estadoMesa() != null) {
            mesa.setEstadoMesa(request.estadoMesa());
        }

        Mesa actualizada = mesaRepository.save(mesa);

        logger.info(
                "Mesa actualizada. idMesa={}",
                actualizada.getIdMesa()
        );

        return toResponse(actualizada);
    }

    // ELIMINAR MESA
    @Transactional
    public void eliminar(UUID id) {

        Mesa mesa = buscarEntidad(id);

        mesaRepository.delete(mesa);

        logger.info(
                "Mesa eliminada. idMesa={}",
                mesa.getIdMesa()
        );
    }

// VALIDAR NÚMERO ÚNICO POR SUCURSAL
private void validarNumeroDisponible(
        UUID idSucursal,
        Integer numero,
        UUID idMesaExcluir
) {

    boolean duplicado;

    if (idMesaExcluir == null) {
        duplicado = mesaRepository
                .existsBySucursal_IdSucursalAndNumero(
                        idSucursal,
                        numero
                );
    } else {
        duplicado = mesaRepository
                .existsBySucursal_IdSucursalAndNumeroAndIdMesaNot(
                        idSucursal,
                        numero,
                        idMesaExcluir
                );
    }

    if (duplicado) {
        throw new IllegalArgumentException(
                "Ya existe una mesa con el número "
                        + numero + " en esta sucursal"
        );
    }
}

    // BUSCAR ENTIDAD
    private Mesa buscarEntidad(UUID id) {
        return mesaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Mesa no encontrada: " + id
                        )
                );
    }

    // CONVERTIR A RESPONSE
    private MesaResponse toResponse(Mesa mesa) {
        return new MesaResponse(
                mesa.getIdMesa(),
                mesa.getSucursal().getIdSucursal(),
                mesa.getNumero(),
                mesa.getCapacidad(),
                mesa.getUbicacion(),
                mesa.getEstadoMesa()
        );
    }
}