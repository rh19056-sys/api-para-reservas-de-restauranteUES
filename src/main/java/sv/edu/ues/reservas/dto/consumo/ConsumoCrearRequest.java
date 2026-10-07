package sv.edu.ues.reservas.dto.consumo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import sv.edu.ues.reservas.validation.Dinero;

import java.math.BigDecimal;
import java.util.UUID;

public record ConsumoCrearRequest(

        @NotNull(message = "El producto es obligatorio")
        UUID idProducto,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor que 0")
        Integer cantidad,

        @NotNull(message = "El precio unitario es obligatorio")
        @Dinero
        BigDecimal precioUnitario
) {
}

//--Se usa para platos, menús y bebidas de una reserva.
//--TODO: cuando existan Plato/Menu/Bebida, tomar el precio del catálogo.
