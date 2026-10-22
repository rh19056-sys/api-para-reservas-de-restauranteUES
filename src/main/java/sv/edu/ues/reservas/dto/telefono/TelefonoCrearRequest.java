package sv.edu.ues.reservas.dto.telefono;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record TelefonoCrearRequest(

        @NotNull(message = "La cuenta es obligatoria")
        UUID idCuenta,

        @NotBlank(message = "El número de teléfono es obligatorio")
        @Size(
                max = 30,
                message = "El número de teléfono no puede superar 30 caracteres"
        )
        String numeroTelefono,

        Boolean esPrincipal,

        String estadoContacto
) {
}