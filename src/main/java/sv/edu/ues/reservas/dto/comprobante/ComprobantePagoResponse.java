package sv.edu.ues.reservas.dto.comprobante;

import sv.edu.ues.reservas.domain.enums.FormatoComprobante;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record ComprobantePagoResponse(
        UUID idComprobante,
        UUID idPago,
        String numeroComprobante,
        OffsetDateTime fechaEmision,
        FormatoComprobante formato,
        String referencia,
        Map<String, Object> desglose
) {
}
