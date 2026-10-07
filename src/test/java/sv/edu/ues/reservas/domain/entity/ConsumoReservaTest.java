package sv.edu.ues.reservas.domain.entity;

import org.junit.jupiter.api.Test;
import sv.edu.ues.reservas.domain.entity.id.ReservaPlatoId;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ConsumoReservaTest {

    @Test
    void calculaSubtotal() {
        ReservaPlato consumo = new ReservaPlato(
                new ReservaPlatoId(UUID.randomUUID(), UUID.randomUUID())
        );
        consumo.setCantidad(3);
        consumo.setPrecioUnitario(new BigDecimal("8.50"));

        consumo.calcularSubtotal();

        assertThat(consumo.getSubtotal()).isEqualByComparingTo("25.50");
    }
}
