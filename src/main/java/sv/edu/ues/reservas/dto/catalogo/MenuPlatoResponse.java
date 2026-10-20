package sv.edu.ues.reservas.dto.catalogo;

import java.util.UUID;

public record MenuPlatoResponse(
        UUID idPlato,
        String nombrePlato,
        Integer cantidad,
        Integer orden
) {
}
