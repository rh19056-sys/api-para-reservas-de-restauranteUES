package sv.edu.ues.reservas.dto.horariorestaurante;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalTime;

public record HorarioRestauranteActualizarRequest(

        @Min(value = 1, message = "El día debe estar entre 1 y 7")
        @Max(value = 7, message = "El día debe estar entre 1 y 7")
        Short diaSemana,

        LocalTime horaApertura,

        LocalTime horaCierre,

        LocalDate fechaInicio,

        LocalDate fechaFin

) {
}