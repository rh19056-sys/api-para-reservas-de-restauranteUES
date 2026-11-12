package sv.edu.ues.reservas.dto.politicareserva;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record PoliticaReservaCrearRequest(

        @NotNull(message = "El identificador de la sucursal es obligatorio")
        UUID idSucursal,

        @NotBlank(message = "El tipo de política es obligatorio")
        @Size(max = 100, message = "El tipo no puede superar 100 caracteres")
        String tipo,

        String descripcion,

        LocalDate fechaInicio,

        LocalDate fechaFin,

        String casosAplicacion
) {}