package sv.edu.ues.reservas.domain.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ConsumoReservaTest {

    @Test
    void construyeIdDesdeRelacionesYCalculaSubtotal() {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(UUID.randomUUID());
        Plato plato = new Plato();
        plato.setIdPlato(UUID.randomUUID());

        ReservaPlato consumo = new ReservaPlato(reserva, plato);
        consumo.setCantidad(3);
        consumo.setPrecioUnitario(new BigDecimal("8.50"));

        consumo.calcularSubtotal();

        assertThat(consumo.getId().getIdReserva()).isEqualTo(reserva.getIdReserva());
        assertThat(consumo.getId().idProducto()).isEqualTo(plato.getIdPlato());
        assertThat(consumo.getSubtotal()).isEqualByComparingTo("25.50");
    }
}
