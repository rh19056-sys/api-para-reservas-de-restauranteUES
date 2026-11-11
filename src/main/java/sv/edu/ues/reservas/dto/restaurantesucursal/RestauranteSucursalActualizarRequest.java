package sv.edu.ues.reservas.dto.restaurantesucursal;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RestauranteSucursalActualizarRequest(

        @NotBlank 
        @Size(
                max = 600,
                message = "La dirección de la sucursal debe tener entre 1 y 600 caracteres"
        )
        String direccion,

        @Min(
                value = 1,
                message = "La capacidad debe ser mayor que cero"
        )
        Integer capacidad,

        @Size(
                max = 100,
                message = "El tipo de cocina no puede superar los 100 caracteres"
        )
        String tipoCocina

) {
}