package sv.edu.ues.reservas.dto.reserva;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import sv.edu.ues.reservas.domain.enums.EstadoReserva;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ReservaActualizarRequest(
        UUID idCuentaCliente,
        UUID idSucursal,
        UUID idPolitica,
        UUID idEvento,
        @Min(1) Integer numeroComensales,
        OffsetDateTime horaInicio,
        OffsetDateTime horaFin,
        EstadoReserva estadoReserva,
        @Size(max = 2000) String motivoCancelacion,
        List<UUID> idsMesas
) {
}
