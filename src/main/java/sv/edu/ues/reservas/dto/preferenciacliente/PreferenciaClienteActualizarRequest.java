package sv.edu.ues.reservas.dto.preferenciacliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PreferenciaClienteActualizarRequest(

        @NotBlank(message = "El tipo de preferencia es obligatorio")
        @Size(max = 100, message = "El tipo no puede superar 100 caracteres")
        String tipo,

        @NotBlank(message = "El detalle de la preferencia es obligatorio")
        String detalle

) {
}