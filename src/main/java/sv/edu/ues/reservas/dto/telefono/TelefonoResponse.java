package sv.edu.ues.reservas.dto.telefono;

import sv.edu.ues.reservas.domain.enums.EstadoContacto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TelefonoResponse(
        UUID idTelefono,
        UUID idCuenta,
        String numeroTelefono,
        EstadoContacto estadoContacto,
        Boolean esPrincipal,
        OffsetDateTime fechaCreacion
) {
}