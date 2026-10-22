package sv.edu.ues.reservas.dto.evento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sv.edu.ues.reservas.domain.enums.EstadoEvento;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record EventoCrearRequest(
        @NotNull UUID idSucursal,
        @NotBlank @Size(max = 200) String titulo,
        @NotBlank @Size(max = 100) String tipo,
        @NotNull OffsetDateTime fechaEvento,
        EstadoEvento estadoEvento,
        List<@Valid EventoItemRequest> menus,
        List<@Valid EventoItemRequest> platos,
        List<@Valid EventoItemRequest> bebidas
) {
}
