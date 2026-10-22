package sv.edu.ues.reservas.dto.reserva;

import sv.edu.ues.reservas.domain.enums.EstadoReserva;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ReservaResponse(
        UUID idReserva,
        UUID idCuentaCliente,
        UUID idSucursal,
        UUID idPolitica,
        UUID idEvento,
        Integer numeroComensales,
        OffsetDateTime horaInicio,
        OffsetDateTime horaFin,
        EstadoReserva estadoReserva,
        String motivoCancelacion,
        OffsetDateTime fechaCreacion,
        List<UUID> idsMesas
) {
}
