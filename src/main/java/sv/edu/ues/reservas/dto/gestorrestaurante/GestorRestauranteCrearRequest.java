package sv.edu.ues.reservas.dto.gestorrestaurante;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record GestorRestauranteCrearRequest(

        @NotNull(message = "El identificador de la cuenta es obligatorio")
        UUID idCuenta,

        @NotNull(message = "El identificador de la sucursal es obligatorio")
        UUID idSucursal

) {
}