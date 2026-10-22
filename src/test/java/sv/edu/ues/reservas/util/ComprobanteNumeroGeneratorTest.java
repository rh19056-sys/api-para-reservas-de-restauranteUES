package sv.edu.ues.reservas.util;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ComprobanteNumeroGeneratorTest {

    private final ComprobanteNumeroGenerator generator =
            new ComprobanteNumeroGenerator(
                    Clock.fixed(
                            Instant.parse("2026-10-07T15:00:00Z"),
                            ZoneId.of("America/El_Salvador")
                    )
            );

    @Test
    void usaFormatoCpFechaSufijo() {
        assertThat(generator.generar())
                .matches("CP-20261007-[0-9A-F]{8}");
    }

    @Test
    void cabeEnLaColumnaDeLaBd() {
        // numero_comprobante VARCHAR(100)
        assertThat(generator.generar()).hasSizeLessThanOrEqualTo(100);
    }

    @Test
    void generaNumerosDistintos() {
        Set<String> numeros = new HashSet<>();

        for (int i = 0; i < 1000; i++) {
            numeros.add(generator.generar());
        }

        assertThat(numeros).hasSize(1000);
    }
}
