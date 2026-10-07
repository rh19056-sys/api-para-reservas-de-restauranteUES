package sv.edu.ues.reservas.validation;

import org.junit.jupiter.api.Test;
import sv.edu.ues.reservas.domain.entity.Pago;
import sv.edu.ues.reservas.domain.enums.EstadoPago;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PagoValidatorTest {

    private final PagoValidator validator = new PagoValidator();

    @Test
    void transicionesPermitidas() {
        assertThatCode(() -> {
            validator.validarTransicion(EstadoPago.PENDIENTE, EstadoPago.APROBADO);
            validator.validarTransicion(EstadoPago.PENDIENTE, EstadoPago.RECHAZADO);
            validator.validarTransicion(EstadoPago.APROBADO, EstadoPago.REEMBOLSADO);
            validator.validarTransicion(EstadoPago.APROBADO, EstadoPago.APROBADO);
        }).doesNotThrowAnyException();
    }

    @Test
    void transicionesProhibidas() {
        assertThatThrownBy(() -> validator.validarTransicion(EstadoPago.RECHAZADO, EstadoPago.APROBADO))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> validator.validarTransicion(EstadoPago.PENDIENTE, EstadoPago.REEMBOLSADO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void comprobanteSoloParaAprobados() {
        Pago pendiente = Pago.builder().estadoPago(EstadoPago.PENDIENTE).build();

        assertThatThrownBy(() -> validator.validarEmisionComprobante(pendiente))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void noSeEliminaPagoAprobado() {
        Pago aprobado = Pago.builder().estadoPago(EstadoPago.APROBADO).build();

        assertThatThrownBy(() -> validator.validarEliminacion(aprobado))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
