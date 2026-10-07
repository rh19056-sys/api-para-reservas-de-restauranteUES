package sv.edu.ues.reservas.dto.consumo;

import jakarta.validation.constraints.Min;
import sv.edu.ues.reservas.validation.Dinero;

import java.math.BigDecimal;

public record ConsumoActualizarRequest(

        @Min(value = 1, message = "La cantidad debe ser mayor que 0")
        Integer cantidad,

        @Dinero
        BigDecimal precioUnitario
) {
}
