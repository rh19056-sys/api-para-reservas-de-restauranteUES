package sv.edu.ues.reservas.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import sv.edu.ues.reservas.dto.consumo.ConsumoCrearRequest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DineroTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    private ConsumoCrearRequest conPrecio(String precio) {
        return new ConsumoCrearRequest(UUID.randomUUID(), 1, new BigDecimal(precio));
    }

    @Test
    void aceptaImporteValido() {
        assertThat(validator.validate(conPrecio("8.50"))).isEmpty();
        assertThat(validator.validate(conPrecio("0.00"))).isEmpty();
    }

    @Test
    void rechazaNegativoYTresDecimales() {
        assertThat(validator.validate(conPrecio("-1.00"))).isNotEmpty();
        assertThat(validator.validate(conPrecio("1.999"))).isNotEmpty();
    }
}
