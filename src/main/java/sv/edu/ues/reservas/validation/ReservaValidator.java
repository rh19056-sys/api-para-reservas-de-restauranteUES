package sv.edu.ues.reservas.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sv.edu.ues.reservas.domain.entity.Reserva;
import sv.edu.ues.reservas.domain.enums.EstadoReserva;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.ReservaRepository;

import java.util.Set;
import java.util.UUID;

// Reglas que deben cumplir las reservas para recibir consumos o pagos.
@Component
@RequiredArgsConstructor
public class ReservaValidator {

    // Se agregan/modifican productos mientras la reserva sigue vigente.
    private static final Set<EstadoReserva> ADMITEN_CONSUMOS =
            Set.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA);

    // Se puede pagar también después de la visita (COMPLETADA).
    private static final Set<EstadoReserva> ADMITEN_PAGOS =
            Set.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA, EstadoReserva.COMPLETADA);

    private final ReservaRepository reservaRepository;

    public Reserva obtenerParaConsumo(UUID idReserva) {

        Reserva reserva = obtener(idReserva);

        if (!ADMITEN_CONSUMOS.contains(reserva.getEstadoReserva())) {
            throw new IllegalArgumentException(
                    "No se pueden modificar los consumos de una reserva "
                            + reserva.getEstadoReserva()
            );
        }

        return reserva;
    }

    public Reserva obtenerParaPago(UUID idReserva) {

        Reserva reserva = obtener(idReserva);

        if (!ADMITEN_PAGOS.contains(reserva.getEstadoReserva())) {
            throw new IllegalArgumentException(
                    "No se pueden registrar pagos en una reserva "
                            + reserva.getEstadoReserva()
            );
        }

        return reserva;
    }

    // Mismo criterio que EventoService: los productos deben ser de la sucursal.
    public void validarMismaSucursal(
            Reserva reserva,
            UUID idSucursalProducto,
            String tipo
    ) {

        if (!reserva.getSucursal().getIdSucursal().equals(idSucursalProducto)) {
            throw new IllegalArgumentException(
                    tipo + " debe pertenecer a la misma sucursal que la reserva"
            );
        }
    }

    public Reserva obtener(UUID idReserva) {

        return reservaRepository.findById(idReserva)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Reserva no encontrada: " + idReserva
                        )
                );
    }
}
