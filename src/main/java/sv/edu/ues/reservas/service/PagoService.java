package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sv.edu.ues.reservas.domain.entity.Pago;
import sv.edu.ues.reservas.domain.enums.EstadoPago;
import sv.edu.ues.reservas.domain.enums.MetodoPago;
import sv.edu.ues.reservas.dto.pago.PagoActualizarRequest;
import sv.edu.ues.reservas.dto.pago.PagoCrearRequest;
import sv.edu.ues.reservas.dto.pago.PagoResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.PagoRepository;
import sv.edu.ues.reservas.util.EnumParser;
import sv.edu.ues.reservas.validation.PagoValidator;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PagoValidator pagoValidator;

    @Transactional
    public PagoResponse crear(PagoCrearRequest request) {

        // TODO: validar que la reserva exista y no esté CANCELADA
        // cuando ReservaRepository esté disponible en main.
        Pago pago = Pago.builder()
                .idReserva(request.idReserva())
                .metodoPago(
                        EnumParser.parse(
                                MetodoPago.class,
                                request.metodoPago(),
                                "Método de pago"
                        )
                )
                .monto(request.monto())
                .estadoPago(
                        EnumParser.parseOrDefault(
                                EstadoPago.class,
                                request.estadoPago(),
                                "Estado de pago",
                                EstadoPago.PENDIENTE
                        )
                )
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

        if (StringUtils.hasText(request.metodoPago())) {

            pagoValidator.validarCambioMetodo(pago);

            pago.setMetodoPago(
                    EnumParser.parse(
                            MetodoPago.class,
                            request.metodoPago(),
                            "Método de pago"
                    )
            );
        }

        if (StringUtils.hasText(request.estadoPago())) {

            EstadoPago nuevo = EnumParser.parse(
                    EstadoPago.class,
                    request.estadoPago(),
                    "Estado de pago"
            );

            pagoValidator.validarTransicion(pago.getEstadoPago(), nuevo);
            pago.setEstadoPago(nuevo);
        }

        return toResponse(pagoRepository.save(pago));
    }

    @Transactional
    public void eliminar(UUID id) {

        Pago pago = buscarEntidad(id);

        pagoValidator.validarEliminacion(pago);

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
