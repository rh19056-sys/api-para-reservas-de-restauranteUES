package sv.edu.ues.reservas.dto.restaurantesucursal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RestauranteSucursalCrearRequest(

        @NotNull(message = "La organización de restaurantes es obligatoria")
        UUID idRestauranteOrg,

        @NotBlank(message = "La dirección de la sucursal es obligatoria")
        @Size(
                max = 300,
                message = "La dirección no puede superar los 300 caracteres"
        )
        String direccion,

        @NotNull(message = "La capacidad de la sucursal es obligatoria")
        @Min(value = 1, message = "La capacidad debe ser mayor que cero")
        Integer capacidad,

        @Size(
                max = 100,
                message = "El tipo de cocina no puede superar los 100 caracteres"
        )
        String tipoCocina

) {
}