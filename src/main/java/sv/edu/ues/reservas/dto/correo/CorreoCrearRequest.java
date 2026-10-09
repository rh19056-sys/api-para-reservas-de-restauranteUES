package sv.edu.ues.reservas.dto.correo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CorreoCrearRequest(

        @NotNull(message = "La cuenta es obligatoria")
        UUID idCuenta,

        @NotBlank(message = "La dirección de correo es obligatoria")
        @Email(message = "La dirección de correo no tiene un formato válido")
        @Size(max = 255, message = "El correo no puede superar 255 caracteres")
        String direccionCorreo,

        Boolean esPrincipal,

        String estadoContacto
) {
}