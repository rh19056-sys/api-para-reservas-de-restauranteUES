package sv.edu.ues.reservas.dto.preferenciacliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record PreferenciaClienteCrearRequest(

        @NotNull(message = "El identificador del cliente es obligatorio")
        UUID idCuenta,

        @NotBlank(message = "El tipo de preferencia es obligatorio")
        @Size(max = 100, message = "El tipo no puede superar 100 caracteres")
        String tipo,

        @NotBlank(message = "El detalle de la preferencia es obligatorio")
        String detalle

) {
}
