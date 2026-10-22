package sv.edu.ues.reservas.dto.catalogo;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sv.edu.ues.reservas.domain.enums.EstadoMenu;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record MenuCrearRequest(
        @NotNull UUID idSucursal,
        @NotBlank @Size(max = 150) String nombre,
        @Size(max = 5000) String descripcion,
        @Size(max = 100) String temporada,
        @Min(1) Integer cantidadPersonas,
        @NotNull @DecimalMin("0.00") @Digits(integer = 10, fraction = 2)
        BigDecimal precio,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        EstadoMenu estadoMenu
) {
}
