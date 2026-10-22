package sv.edu.ues.reservas.dto.reserva;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sv.edu.ues.reservas.domain.enums.EstadoReserva;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ReservaCrearRequest(
        @NotNull UUID idCuentaCliente,
        @NotNull UUID idSucursal,
        UUID idPolitica,
        UUID idEvento,
        @NotNull @Min(1) Integer numeroComensales,
        @NotNull OffsetDateTime horaInicio,
        @NotNull OffsetDateTime horaFin,
        EstadoReserva estadoReserva,
        @Size(max = 2000) String motivoCancelacion,
        @NotEmpty List<@NotNull UUID> idsMesas
) {
}
