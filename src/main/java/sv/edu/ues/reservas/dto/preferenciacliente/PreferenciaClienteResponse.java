package sv.edu.ues.reservas.dto.preferenciacliente;

import java.util.UUID;

public record PreferenciaClienteResponse(
        UUID idPreferencia,
        UUID idCuenta,
        String tipo,
        String detalle
) {
}