package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.Pago;
import sv.edu.ues.reservas.domain.enums.EstadoPago;
import sv.edu.ues.reservas.domain.enums.MetodoPago;
import sv.edu.ues.reservas.dto.pago.PagoActualizarRequest;
import sv.edu.ues.reservas.dto.pago.PagoCrearRequest;
import sv.edu.ues.reservas.dto.pago.PagoResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.PagoRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;

    @Transactional
    public PagoResponse crear(PagoCrearRequest request) {

        // TODO: validar que la reserva exista y no esté CANCELADA
        // cuando ReservaRepository esté disponible en main.
        Pago pago = Pago.builder()
                .idReserva(request.idReserva())
                .metodoPago(parseMetodo(request.metodoPago()))
                .monto(request.monto())
                .estadoPago(parseEstado(request.estadoPago()))
                .build();

        return toResponse(pagoRepository.save(pago));
    }

    @Transactional
    public List<PagoResponse> obtenerTodos() {

        return pagoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public List<PagoResponse> obtenerPorReserva(UUID idReserva) {

        return pagoRepository.findByIdReserva(idReserva)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PagoResponse obtenerPorId(UUID id) {

        return toResponse(buscarEntidad(id));
    }

    @Transactional
    public PagoResponse actualizar(
            UUID id,
            PagoActualizarRequest request
    ) {

        Pago pago = buscarEntidad(id);

        if (request.metodoPago() != null
                && !request.metodoPago().isBlank()) {

            if (pago.getEstadoPago() != EstadoPago.PENDIENTE) {
                throw new IllegalArgumentException(
                        "Solo se puede cambiar el método de un pago PENDIENTE"
                );
            }

            pago.setMetodoPago(parseMetodo(request.metodoPago()));
        }

        if (request.estadoPago() != null
                && !request.estadoPago().isBlank()) {

            EstadoPago nuevo = parseEstado(request.estadoPago());
            validarTransicion(pago.getEstadoPago(), nuevo);
            pago.setEstadoPago(nuevo);
        }

        return toResponse(pagoRepository.save(pago));
    }

    @Transactional
    public void eliminar(UUID id) {

        Pago pago = buscarEntidad(id);

        if (pago.getEstadoPago() == EstadoPago.APROBADO
                || pago.getEstadoPago() == EstadoPago.REEMBOLSADO) {

            throw new IllegalArgumentException(
                    "No se puede eliminar un pago "
                            + pago.getEstadoPago()
            );
        }

        pagoRepository.delete(pago);
    }

    Pago buscarEntidad(UUID id) {

        return pagoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Pago no encontrado: " + id
                        )
                );
    }

    // PENDIENTE -> APROBADO | RECHAZADO
    // APROBADO  -> REEMBOLSADO
    // RECHAZADO y REEMBOLSADO son estados finales.
    private void validarTransicion(EstadoPago actual, EstadoPago nuevo) {

        if (actual == nuevo) {
            return;
        }

        Set<EstadoPago> permitidos = switch (actual) {
            case PENDIENTE -> Set.of(EstadoPago.APROBADO, EstadoPago.RECHAZADO);
            case APROBADO -> Set.of(EstadoPago.REEMBOLSADO);
            case RECHAZADO, REEMBOLSADO -> Set.of();
        };

        if (!permitidos.contains(nuevo)) {
            throw new IllegalArgumentException(
                    "Transición de pago inválida: "
                            + actual + " -> " + nuevo
            );
        }
    }

    private MetodoPago parseMetodo(String valor) {

        try {
            return MetodoPago.valueOf(
                    valor.trim().toUpperCase()
            );

        } catch (IllegalArgumentException ex) {

            throw new IllegalArgumentException(
                    "Método de pago inválido: " + valor
            );
        }
    }

    private EstadoPago parseEstado(String valor) {

        if (valor == null || valor.isBlank()) {
            return EstadoPago.PENDIENTE;
        }

        try {
            return EstadoPago.valueOf(
                    valor.trim().toUpperCase()
            );

        } catch (IllegalArgumentException ex) {

            throw new IllegalArgumentException(
                    "Estado de pago inválido: " + valor
            );
        }
    }

    private PagoResponse toResponse(Pago pago) {

        return new PagoResponse(
                pago.getIdPago(),
                pago.getIdReserva(),
                pago.getMetodoPago(),
                pago.getMonto(),
                pago.getEstadoPago(),
                pago.getFechaPago()
        );
    }
}
