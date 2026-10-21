package sv.edu.ues.reservas.dto.catalogo;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import sv.edu.ues.reservas.domain.enums.EstadoProducto;

import java.math.BigDecimal;

public record PlatoActualizarRequest(
        @Size(max = 150) String nombre,
        @Size(max = 5000) String descripcion,
        @DecimalMin("0.00") @Digits(integer = 10, fraction = 2) BigDecimal precio,
        @Size(max = 100) String categoria,
        EstadoProducto estadoProducto
) {
}
