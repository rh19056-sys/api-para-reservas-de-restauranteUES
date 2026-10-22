package sv.edu.ues.reservas.dto.catalogo;

import sv.edu.ues.reservas.domain.enums.EstadoProducto;

import java.math.BigDecimal;
import java.util.UUID;

public record PlatoResponse(
        UUID idPlato,
        UUID idSucursal,
        String nombre,
        String descripcion,
        BigDecimal precio,
        String categoria,
        EstadoProducto estadoProducto
) {
}
