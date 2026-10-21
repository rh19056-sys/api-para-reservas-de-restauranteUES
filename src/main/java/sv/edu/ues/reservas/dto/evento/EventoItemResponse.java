package sv.edu.ues.reservas.dto.evento;

import java.math.BigDecimal;
import java.util.UUID;

public record EventoItemResponse(
        UUID id,
        Integer cantidad,
        BigDecimal precioUnitario
) {
}
