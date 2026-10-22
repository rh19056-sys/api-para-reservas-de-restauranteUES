package sv.edu.ues.reservas.dto.telefono;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TelefonoActualizarRequest(

        @NotBlank(message = "El número de teléfono no puede estar vacío")
        @Size(
                max = 30,
                message = "El número de teléfono no puede superar 30 caracteres"
        )
        String numeroTelefono,

        Boolean esPrincipal,

        String estadoContacto
) {
}