package sv.edu.ues.reservas.dto.catalogo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sv.edu.ues.reservas.domain.enums.EstadoProducto;

import java.util.UUID;

public record BebidaCrearRequest(
        @NotNull UUID idSucursal,
        @NotBlank @Size(max = 150) String nombre,
        @Size(max = 5000) String descripcion,
        EstadoProducto estadoBebida
) {
}
