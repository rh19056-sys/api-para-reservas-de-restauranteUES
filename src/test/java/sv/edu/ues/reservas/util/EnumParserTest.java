package sv.edu.ues.reservas.util;

import org.junit.jupiter.api.Test;
import sv.edu.ues.reservas.domain.enums.EstadoPago;
import sv.edu.ues.reservas.domain.enums.MetodoPago;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnumParserTest {

    @Test
    void ignoraMayusculasYEspacios() {
        assertThat(EnumParser.parse(MetodoPago.class, " tarjeta ", "Método"))
                .isEqualTo(MetodoPago.TARJETA);
    }

    @Test
    void valorInvalidoLanzaMensajeLegible() {
        assertThatThrownBy(() -> EnumParser.parse(MetodoPago.class, "bitcoin", "Método de pago"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Método de pago inválido: bitcoin");
    }

    @Test
    void vacioEsObligatorioEnParse() {
        assertThatThrownBy(() -> EnumParser.parse(MetodoPago.class, " ", "Método de pago"))
                .hasMessage("Método de pago es obligatorio");
    }

    @Test
    void vacioDevuelveDefaultEnParseOrDefault() {
        assertThat(EnumParser.parseOrDefault(EstadoPago.class, null, "Estado", EstadoPago.PENDIENTE))
                .isEqualTo(EstadoPago.PENDIENTE);
    }
}
