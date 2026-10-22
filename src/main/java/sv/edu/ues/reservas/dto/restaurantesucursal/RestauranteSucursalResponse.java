package sv.edu.ues.reservas.dto.restaurantesucursal;

import sv.edu.ues.reservas.domain.enums.EstadoSucursal;

import java.util.UUID;

public record RestauranteSucursalResponse(
        UUID idSucursal,
        UUID idRestauranteOrg,
        String direccion,
        Integer capacidad,
        String tipoCocina,
        EstadoSucursal estadoSucursal
) {
}