package sv.edu.ues.reservas.dto.horariorestaurante;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record HorarioRestauranteResponse(

        UUID idHorario,

        UUID idSucursal,

        Short diaSemana,

        LocalTime horaApertura,

        LocalTime horaCierre,

        LocalDate fechaInicio,

        LocalDate fechaFin

) {
}