package sv.edu.ues.reservas.dto.correo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CorreoActualizarRequest(

        @Email(message = "La dirección de correo no tiene un formato válido")
        @Size(max = 255, message = "El correo no puede superar 255 caracteres")
        String direccionCorreo,

        Boolean esPrincipal,

        String estadoContacto
) {
}