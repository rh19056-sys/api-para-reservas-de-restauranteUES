package sv.edu.ues.reservas.dto.gestorrestaurante;

import jakarta.validation.constraints.NotNull;
import sv.edu.ues.reservas.domain.enums.EstadoGestor;

public record GestorRestauranteActualizarRequest(

        @NotNull(message = "El estado del gestor es obligatorio")
        EstadoGestor estadoGestor

) {
}