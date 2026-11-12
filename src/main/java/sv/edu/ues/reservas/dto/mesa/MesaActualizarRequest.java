package sv.edu.ues.reservas.dto.mesa;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import sv.edu.ues.reservas.domain.enums.EstadoMesa;

public record MesaActualizarRequest(

        @Min(value = 1, message = "El número debe ser mayor a 0")
        Integer numero,

        @Min(value = 1, message = "La capacidad debe ser mayor a 0")
        Integer capacidad,

        @Size(max = 100, message = "La ubicación no puede superar 100 caracteres")
        String ubicacion,

        EstadoMesa estadoMesa

) {
}