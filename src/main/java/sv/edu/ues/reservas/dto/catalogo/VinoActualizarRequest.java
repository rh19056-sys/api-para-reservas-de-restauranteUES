package sv.edu.ues.reservas.dto.catalogo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

public record VinoActualizarRequest(
        @Size(max = 150) String marca,
        @Min(0) Integer stock,
        @Size(max = 5000) String condicionesConservacion,
        @Min(1800) @Max(2100) Integer anada
) {
}
