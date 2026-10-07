package sv.edu.ues.reservas.dto.pago;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import sv.edu.ues.reservas.validation.Dinero;

import java.math.BigDecimal;
import java.util.UUID;

public record PagoCrearRequest(

        @NotNull(message = "La reserva es obligatoria")
        UUID idReserva,

        @NotBlank(message = "El método de pago es obligatorio")
        String metodoPago,

        @NotNull(message = "El monto es obligatorio")
        @Positive(message = "El monto debe ser mayor que 0")
        @Dinero
        BigDecimal monto,

        String estadoPago
) {
}
