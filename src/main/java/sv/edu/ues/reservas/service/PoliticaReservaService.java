package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import sv.edu.ues.reservas.domain.entity.PoliticaReserva;
import sv.edu.ues.reservas.domain.entity.RestauranteSucursal;

import sv.edu.ues.reservas.dto.politicareserva.PoliticaReservaActualizarRequest;
import sv.edu.ues.reservas.dto.politicareserva.PoliticaReservaCrearRequest;
import sv.edu.ues.reservas.dto.politicareserva.PoliticaReservaResponse;

import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;

import sv.edu.ues.reservas.repository.PoliticaReservaRepository;
import sv.edu.ues.reservas.repository.RestauranteSucursalRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PoliticaReservaService {

    private static final Logger logger =
            LoggerFactory.getLogger(PoliticaReservaService.class);

    private final PoliticaReservaRepository politicaReservaRepository;
    private final RestauranteSucursalRepository restauranteSucursalRepository;

    @Transactional
    public PoliticaReservaResponse crear(
            PoliticaReservaCrearRequest request
    ) {

        RestauranteSucursal sucursal = restauranteSucursalRepository
                .findById(request.idSucursal())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Sucursal no encontrada: " + request.idSucursal()
                        )
                );

        validarFechas(
                request.fechaInicio(),
                request.fechaFin()
        );

        PoliticaReserva politica = new PoliticaReserva();

        politica.setSucursal(sucursal);
        politica.setTipo(request.tipo().trim());
        politica.setDescripcion(request.descripcion());
        politica.setFechaInicio(request.fechaInicio());
        politica.setFechaFin(request.fechaFin());
        politica.setCasosAplicacion(request.casosAplicacion());

        PoliticaReserva guardada =
                politicaReservaRepository.save(politica);

        logger.info(
                "Política de reserva creada. idPolitica={}, idSucursal={}",
                guardada.getIdPolitica(),
                sucursal.getIdSucursal()
        );

        return toResponse(guardada);
    }

    @Transactional
    public List<PoliticaReservaResponse> obtenerTodas() {

        return politicaReservaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PoliticaReservaResponse obtenerPorId(UUID id) {

        return toResponse(buscarEntidad(id));
    }

    @Transactional
    public List<PoliticaReservaResponse> obtenerPorSucursal(
            UUID idSucursal
    ) {

        if (!restauranteSucursalRepository.existsById(idSucursal)) {
            throw new RecursoNoEncontradoException(
                    "Sucursal no encontrada: " + idSucursal
            );
        }

        return politicaReservaRepository
                .findBySucursal_IdSucursal(idSucursal)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PoliticaReservaResponse actualizar(
            UUID id,
            PoliticaReservaActualizarRequest request
    ) {

        PoliticaReserva politica = buscarEntidad(id);

        LocalDate fechaInicio = request.fechaInicio() != null
                ? request.fechaInicio()
                : politica.getFechaInicio();

        LocalDate fechaFin = request.fechaFin() != null
                ? request.fechaFin()
                : politica.getFechaFin();

        validarFechas(fechaInicio, fechaFin);

        politica.setTipo(request.tipo().trim());

        if (request.descripcion() != null) {
            politica.setDescripcion(request.descripcion());
        }

        politica.setFechaInicio(fechaInicio);
        politica.setFechaFin(fechaFin);

        if (request.casosAplicacion() != null) {
            politica.setCasosAplicacion(request.casosAplicacion());
        }

        PoliticaReserva actualizada =
                politicaReservaRepository.save(politica);

        logger.info(
                "Política de reserva actualizada. idPolitica={}",
                actualizada.getIdPolitica()
        );

        return toResponse(actualizada);
    }

    @Transactional
    public void eliminar(UUID id) {

        PoliticaReserva politica = buscarEntidad(id);

        politicaReservaRepository.delete(politica);

        logger.info(
                "Política de reserva eliminada. idPolitica={}",
                politica.getIdPolitica()
        );
    }

    private PoliticaReserva buscarEntidad(UUID id) {

        return politicaReservaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Política de reserva no encontrada: " + id
                        )
                );
    }

    private void validarFechas(
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {

        if (fechaInicio != null && fechaFin != null
                && fechaFin.isBefore(fechaInicio)) {

            throw new IllegalArgumentException(
                    "La fecha final no puede ser anterior a la fecha inicial"
            );
        }
    }

    private PoliticaReservaResponse toResponse(
            PoliticaReserva politica
    ) {

        return new PoliticaReservaResponse(
                politica.getIdPolitica(),
                politica.getSucursal().getIdSucursal(),
                politica.getTipo(),
                politica.getDescripcion(),
                politica.getFechaInicio(),
                politica.getFechaFin(),
                politica.getCasosAplicacion()
        );
    }
}