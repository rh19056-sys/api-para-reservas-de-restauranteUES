package sv.edu.ues.reservas.dto.horariorestaurante;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record HorarioRestauranteCrearRequest(

        @NotNull(message = "El identificador de la sucursal es obligatorio")
        UUID idSucursal,

        @NotNull(message = "El día de la semana es obligatorio")
        @Min(value = 1, message = "El día debe estar entre 1 y 7")
        @Max(value = 7, message = "El día debe estar entre 1 y 7")
        Short diaSemana,

        @NotNull(message = "La hora de apertura es obligatoria")
        LocalTime horaApertura,

        @NotNull(message = "La hora de cierre es obligatoria")
        LocalTime horaCierre,

        LocalDate fechaInicio,

        LocalDate fechaFin

) {
}