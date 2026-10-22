package sv.edu.ues.reservas.dto.catalogo;

import sv.edu.ues.reservas.domain.enums.EstadoMenu;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record MenuResponse(
        UUID idMenu,
        UUID idSucursal,
        String nombre,
        String descripcion,
        String temporada,
        Integer cantidadPersonas,
        BigDecimal precio,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        EstadoMenu estadoMenu
) {
}
