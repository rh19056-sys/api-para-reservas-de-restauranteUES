package sv.edu.ues.reservas.dto.catalogo;

import java.util.UUID;

public record MenuBebidaResponse(
        UUID idBebida,
        String nombreBebida,
        Integer cantidad,
        Integer orden
) {
}
