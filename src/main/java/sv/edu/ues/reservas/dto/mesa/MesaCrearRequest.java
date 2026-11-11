package sv.edu.ues.reservas.dto.mesa;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record MesaCrearRequest(

        @NotNull(message = "El identificador de la sucursal es obligatorio")
        UUID idSucursal,

        @NotNull(message = "El número de mesa es obligatorio")
        @Min(value = 1, message = "El número debe ser mayor a 0")
        Integer numero,

        @NotNull(message = "La capacidad es obligatoria")
        @Min(value = 1, message = "La capacidad debe ser mayor a 0")
        Integer capacidad,

        @NotBlank(message = "La ubicación es obligatoria")
        @Size(max = 100, message = "La ubicación no puede superar 100 caracteres")
        String ubicacion

) {
}