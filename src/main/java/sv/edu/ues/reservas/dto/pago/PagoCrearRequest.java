package sv.edu.ues.reservas.dto.pago;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record PagoCrearRequest(

        @NotNull(message = "La reserva es obligatoria")
        UUID idReserva,

        @NotBlank(message = "El método de pago es obligatorio")
        String metodoPago,

        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(
                value = "0.01",
                message = "El monto debe ser mayor que 0"
        )
        @Digits(
                integer = 10,
                fraction = 2,
                message = "El monto admite máximo 2 decimales"
        )
        BigDecimal monto,

        String estadoPago
) {
}
