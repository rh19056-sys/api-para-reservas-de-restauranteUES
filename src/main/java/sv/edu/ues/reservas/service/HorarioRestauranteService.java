package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import sv.edu.ues.reservas.domain.entity.HorarioRestaurante;
import sv.edu.ues.reservas.domain.entity.RestauranteSucursal;

import sv.edu.ues.reservas.dto.horariorestaurante.HorarioRestauranteActualizarRequest;
import sv.edu.ues.reservas.dto.horariorestaurante.HorarioRestauranteCrearRequest;
import sv.edu.ues.reservas.dto.horariorestaurante.HorarioRestauranteResponse;

import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;

import sv.edu.ues.reservas.repository.HorarioRestauranteRepository;
import sv.edu.ues.reservas.repository.RestauranteSucursalRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HorarioRestauranteService {

    private static final Logger logger =
            LoggerFactory.getLogger(HorarioRestauranteService.class);

    private final HorarioRestauranteRepository horarioRestauranteRepository;
    private final RestauranteSucursalRepository restauranteSucursalRepository;

    @Transactional
    public HorarioRestauranteResponse crear(
            HorarioRestauranteCrearRequest request
    ) {

        RestauranteSucursal sucursal = restauranteSucursalRepository
                .findById(request.idSucursal())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Sucursal no encontrada: " + request.idSucursal()
                        )
                );

        validarHorario(
                request.horaApertura(),
                request.horaCierre(),
                request.fechaInicio(),
                request.fechaFin()
        );

        HorarioRestaurante horario = new HorarioRestaurante();

        horario.setSucursal(sucursal);
        horario.setDiaSemana(request.diaSemana());
        horario.setHoraApertura(request.horaApertura());
        horario.setHoraCierre(request.horaCierre());
        horario.setFechaInicio(request.fechaInicio());
        horario.setFechaFin(request.fechaFin());

        HorarioRestaurante guardado =
                horarioRestauranteRepository.save(horario);

        logger.info(
                "Horario creado. idHorario={}, idSucursal={}",
                guardado.getIdHorario(),
                sucursal.getIdSucursal()
        );

        return toResponse(guardado);
    }

    @Transactional
    public List<HorarioRestauranteResponse> obtenerTodos() {

        return horarioRestauranteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public HorarioRestauranteResponse obtenerPorId(UUID id) {

        return toResponse(buscarEntidad(id));
    }

    @Transactional
    public List<HorarioRestauranteResponse> obtenerPorSucursal(
            UUID idSucursal
    ) {

        if (!restauranteSucursalRepository.existsById(idSucursal)) {
            throw new RecursoNoEncontradoException(
                    "Sucursal no encontrada: " + idSucursal
            );
        }

        return horarioRestauranteRepository
                .findBySucursal_IdSucursal(idSucursal)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public HorarioRestauranteResponse actualizar(
            UUID id,
            HorarioRestauranteActualizarRequest request
    ) {

        HorarioRestaurante horario = buscarEntidad(id);

        Short diaSemana = request.diaSemana() != null
                ? request.diaSemana()
                : horario.getDiaSemana();

        var horaApertura = request.horaApertura() != null
                ? request.horaApertura()
                : horario.getHoraApertura();

        var horaCierre = request.horaCierre() != null
                ? request.horaCierre()
                : horario.getHoraCierre();

        var fechaInicio = request.fechaInicio() != null
                ? request.fechaInicio()
                : horario.getFechaInicio();

        var fechaFin = request.fechaFin() != null
                ? request.fechaFin()
                : horario.getFechaFin();

        validarHorario(
                horaApertura,
                horaCierre,
                fechaInicio,
                fechaFin
        );

        if (request.diaSemana() != null) {
            horario.setDiaSemana(diaSemana);
        }

        horario.setHoraApertura(horaApertura);
        horario.setHoraCierre(horaCierre);
        horario.setFechaInicio(fechaInicio);
        horario.setFechaFin(fechaFin);

        HorarioRestaurante actualizado =
                horarioRestauranteRepository.save(horario);

        logger.info(
                "Horario actualizado. idHorario={}",
                actualizado.getIdHorario()
        );

        return toResponse(actualizado);
    }

    @Transactional
    public void eliminar(UUID id) {

        HorarioRestaurante horario = buscarEntidad(id);

        horarioRestauranteRepository.delete(horario);

        logger.info(
                "Horario eliminado. idHorario={}",
                horario.getIdHorario()
        );
    }

    private HorarioRestaurante buscarEntidad(UUID id) {

        return horarioRestauranteRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Horario no encontrado: " + id
                        )
                );
    }

    private void validarHorario(
            java.time.LocalTime horaApertura,
            java.time.LocalTime horaCierre,
            java.time.LocalDate fechaInicio,
            java.time.LocalDate fechaFin
    ) {

        if (horaApertura == null || horaCierre == null) {
            throw new IllegalArgumentException(
                    "Las horas de apertura y cierre son obligatorias"
            );
        }

        if (!horaCierre.isAfter(horaApertura)) {
            throw new IllegalArgumentException(
                    "La hora de cierre debe ser posterior a la hora de apertura"
            );
        }

        if (fechaInicio != null && fechaFin != null
                && fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException(
                    "La fecha final no puede ser anterior a la fecha inicial"
            );
        }
    }

    private HorarioRestauranteResponse toResponse(
            HorarioRestaurante horario
    ) {

        return new HorarioRestauranteResponse(
                horario.getIdHorario(),
                horario.getSucursal().getIdSucursal(),
                horario.getDiaSemana(),
                horario.getHoraApertura(),
                horario.getHoraCierre(),
                horario.getFechaInicio(),
                horario.getFechaFin()
        );
    }
}