package sv.edu.ues.reservas.dto.gestorrestaurante;

import sv.edu.ues.reservas.domain.enums.EstadoGestor;

import java.util.UUID;

public record GestorRestauranteResponse(
        UUID idCuenta,
        UUID idSucursal,
        String nombreGestor,
        EstadoGestor estadoGestor
) {
}