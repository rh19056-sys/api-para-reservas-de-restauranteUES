package sv.edu.ues.reservas.dto.consumo;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record ConsumoActualizarRequest(

        @Min(value = 1, message = "La cantidad debe ser mayor que 0")
        Integer cantidad,

        @DecimalMin(
                value = "0.00",
                message = "El precio unitario no puede ser negativo"
        )
        @Digits(
                integer = 10,
                fraction = 2,
                message = "El precio admite máximo 2 decimales"
        )
        BigDecimal precioUnitario
) {
}
