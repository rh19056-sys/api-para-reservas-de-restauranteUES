package sv.edu.ues.reservas.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

// Genera números de comprobante con formato CP-yyyyMMdd-XXXXXXXX.
// No verifica unicidad en BD: eso lo hace ComprobantePagoService.
@Component
public class ComprobanteNumeroGenerator {

    static final String PREFIJO = "CP-";

    private final Clock clock;

    @Autowired
    public ComprobanteNumeroGenerator() {
        this(Clock.systemDefaultZone());
    }

    // Para pruebas: permite fijar la fecha.
    ComprobanteNumeroGenerator(Clock clock) {
        this.clock = clock;
    }

    public String generar() {

        return PREFIJO
                + LocalDate.now(clock).format(DateTimeFormatter.BASIC_ISO_DATE)
                + "-"
                + UUID.randomUUID().toString()
                .substring(0, 8)
                .toUpperCase();
    }
}
