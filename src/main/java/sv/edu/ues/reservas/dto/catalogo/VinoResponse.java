package sv.edu.ues.reservas.dto.catalogo;

import java.util.UUID;

public record VinoResponse(
        UUID idBebida,
        UUID idSucursal,
        String nombre,
        String descripcion,
        String marca,
        Integer stock,
        String condicionesConservacion,
        Integer anada
) {
}
