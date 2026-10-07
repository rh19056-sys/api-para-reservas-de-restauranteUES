package sv.edu.ues.reservas.dto.cuenta;

import sv.edu.ues.reservas.domain.enums.EstadoCuenta;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CuentaResponse(
        UUID idCuenta,
        EstadoCuenta estadoCuenta,
        OffsetDateTime fechaCreacion,
        OffsetDateTime ultimoAcceso,
        OffsetDateTime fechaVerificacion
) {
}