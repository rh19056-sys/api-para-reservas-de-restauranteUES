package sv.edu.ues.reservas.dto.correo;

import sv.edu.ues.reservas.domain.enums.EstadoContacto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CorreoResponse(
        UUID idCorreo,
        UUID idCuenta,
        String direccionCorreo,
        EstadoContacto estadoContacto,
        Boolean esPrincipal,
        OffsetDateTime fechaCreacion
) {
}