package sv.edu.ues.reservas.dto.catalogo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MenuPlatoRequest(
        @NotNull UUID idPlato,
        @NotNull @Min(1) Integer cantidad,
        @Min(1) Integer orden
) {
}
