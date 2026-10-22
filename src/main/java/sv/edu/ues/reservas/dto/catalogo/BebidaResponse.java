package sv.edu.ues.reservas.dto.catalogo;

import sv.edu.ues.reservas.domain.enums.EstadoProducto;

import java.util.UUID;

public record BebidaResponse(
        UUID idBebida,
        UUID idSucursal,
        String nombre,
        String descripcion,
        EstadoProducto estadoBebida,
        boolean vino
) {
}
