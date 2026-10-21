package sv.edu.ues.reservas.dto.evento;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record EventoItemRequest(
        @NotNull UUID id,
        @NotNull @Min(1) Integer cantidad,
        @NotNull @DecimalMin("0.00") @Digits(integer = 10, fraction = 2)
        BigDecimal precioUnitario
) {
}
