package sv.edu.ues.reservas.dto.catalogo;

import jakarta.validation.constraints.Size;
import sv.edu.ues.reservas.domain.enums.EstadoProducto;

public record BebidaActualizarRequest(
        @Size(max = 150) String nombre,
        @Size(max = 5000) String descripcion,
        EstadoProducto estadoBebida
) {
}
