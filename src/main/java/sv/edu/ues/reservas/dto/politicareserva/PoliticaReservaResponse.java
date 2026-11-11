package sv.edu.ues.reservas.dto.politicareserva;

import java.time.LocalDate;
import java.util.UUID;

public record PoliticaReservaResponse(

        UUID idPolitica,

        UUID idSucursal,

        String tipo,

        String descripcion,

        LocalDate fechaInicio,

        LocalDate fechaFin,

        String casosAplicacion
) {}