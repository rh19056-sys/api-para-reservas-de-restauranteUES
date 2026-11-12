package sv.edu.ues.reservas.dto.mesa;

import sv.edu.ues.reservas.domain.enums.EstadoMesa;

import java.util.UUID;

public record MesaResponse(
        UUID idMesa,
        UUID idSucursal,
        Integer numero,
        Integer capacidad,
        String ubicacion,
        EstadoMesa estadoMesa
) {
}