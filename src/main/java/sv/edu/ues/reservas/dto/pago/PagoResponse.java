package sv.edu.ues.reservas.dto.pago;

import sv.edu.ues.reservas.domain.enums.EstadoPago;
import sv.edu.ues.reservas.domain.enums.MetodoPago;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PagoResponse(
        UUID idPago,
        UUID idReserva,
        MetodoPago metodoPago,
        BigDecimal monto,
        EstadoPago estadoPago,
        OffsetDateTime fechaPago
) {
}
