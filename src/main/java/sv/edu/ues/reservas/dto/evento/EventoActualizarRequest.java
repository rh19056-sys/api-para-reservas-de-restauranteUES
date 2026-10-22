package sv.edu.ues.reservas.dto.evento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import sv.edu.ues.reservas.domain.enums.EstadoEvento;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record EventoActualizarRequest(
        UUID idSucursal,
        @Pattern(regexp = ".*\\S.*", message = "El título no puede estar vacío")
        @Size(max = 200) String titulo,
        @Pattern(regexp = ".*\\S.*", message = "El tipo no puede estar vacío")
        @Size(max = 100) String tipo,
        OffsetDateTime fechaEvento,
        EstadoEvento estadoEvento,
        List<@Valid EventoItemRequest> menus,
        List<@Valid EventoItemRequest> platos,
        List<@Valid EventoItemRequest> bebidas
) {
}
