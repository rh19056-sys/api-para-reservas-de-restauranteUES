package sv.edu.ues.reservas.dto.politicareserva;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PoliticaReservaActualizarRequest(

        @NotBlank(message = "El tipo de política no puede estar vacío")
        @Size(max = 100, message = "El tipo no puede superar 100 caracteres")
        String tipo,

        String descripcion,

        LocalDate fechaInicio,

        LocalDate fechaFin,

        String casosAplicacion
) {}