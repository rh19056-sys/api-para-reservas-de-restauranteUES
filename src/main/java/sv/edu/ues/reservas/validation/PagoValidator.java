package sv.edu.ues.reservas.validation;

import org.springframework.stereotype.Component;
import sv.edu.ues.reservas.domain.entity.Pago;
import sv.edu.ues.reservas.domain.enums.EstadoPago;

import java.util.Map;
import java.util.Set;

// Reglas de negocio del ciclo de vida de un pago.
@Component
public class PagoValidator {

    // PENDIENTE -> APROBADO | RECHAZADO
    // APROBADO  -> REEMBOLSADO
    // RECHAZADO y REEMBOLSADO son estados finales.
    private static final Map<EstadoPago, Set<EstadoPago>> TRANSICIONES = Map.of(
            EstadoPago.PENDIENTE, Set.of(EstadoPago.APROBADO, EstadoPago.RECHAZADO),
            EstadoPago.APROBADO, Set.of(EstadoPago.REEMBOLSADO),
            EstadoPago.RECHAZADO, Set.of(),
            EstadoPago.REEMBOLSADO, Set.of()
    );

    public void validarTransicion(EstadoPago actual, EstadoPago nuevo) {

        if (actual != nuevo && !TRANSICIONES.get(actual).contains(nuevo)) {
            throw new IllegalArgumentException(
                    "Transición de pago inválida: " + actual + " -> " + nuevo
            );
        }
    }

    public void validarCambioMetodo(Pago pago) {

        if (pago.getEstadoPago() != EstadoPago.PENDIENTE) {
            throw new IllegalArgumentException(
                    "Solo se puede cambiar el método de un pago PENDIENTE"
            );
        }
    }

    public void validarEliminacion(Pago pago) {

        if (pago.getEstadoPago() == EstadoPago.APROBADO
                || pago.getEstadoPago() == EstadoPago.REEMBOLSADO) {

            throw new IllegalArgumentException(
                    "No se puede eliminar un pago " + pago.getEstadoPago()
            );
        }
    }

    public void validarEmisionComprobante(Pago pago) {

        if (pago.getEstadoPago() != EstadoPago.APROBADO) {
            throw new IllegalArgumentException(
                    "Solo se emite comprobante para pagos APROBADOS"
            );
        }
    }
}
