package sv.edu.ues.reservas.dto.evento;

import sv.edu.ues.reservas.domain.enums.EstadoEvento;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record EventoResponse(
        UUID idEvento,
        UUID idSucursal,
        String titulo,
        String tipo,
        OffsetDateTime fechaEvento,
        EstadoEvento estadoEvento,
        List<EventoItemResponse> menus,
        List<EventoItemResponse> platos,
        List<EventoItemResponse> bebidas
) {
}
