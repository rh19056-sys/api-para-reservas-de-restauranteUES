package sv.edu.ues.reservas.dto.consumo;

import java.math.BigDecimal;
import java.util.UUID;

public record ConsumoResponse(
        UUID idReserva,
        UUID idProducto,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
