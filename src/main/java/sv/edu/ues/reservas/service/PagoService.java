package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import sv.edu.ues.reservas.validation.ReservaValidator;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagoService {

    private static final Logger logger = LoggerFactory.getLogger(PagoService.class);

    private final PagoRepository pagoRepository;
    private final PagoValidator pagoValidator;
    private final ReservaValidator reservaValidator;

    @Transactional
    public PagoResponse crear(PagoCrearRequest request) {

        Pago pago = Pago.builder()
                .reserva(reservaValidator.obtenerParaPago(request.idReserva()))
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

        Pago guardado = pagoRepository.save(pago);
        logger.info(
                "Pago creado. idPago={}, idReserva={}, estado={}",
                guardado.getIdPago(), request.idReserva(), guardado.getEstadoPago()
        );

        return toResponse(guardado);
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

        reservaValidator.obtener(idReserva);

        return pagoRepository.findByReserva_IdReserva(idReserva)
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

        Pago actualizado = pagoRepository.save(pago);
        logger.info(
                "Pago actualizado. idPago={}, estado={}",
                id, actualizado.getEstadoPago()
        );

        return toResponse(actualizado);
    }

    @Transactional
    public void eliminar(UUID id) {

        Pago pago = buscarEntidad(id);

        pagoValidator.validarEliminacion(pago);

        pagoRepository.delete(pago);
        logger.info("Pago eliminado. idPago={}", id);
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
                pago.getReserva().getIdReserva(),
                pago.getMetodoPago(),
                pago.getMonto(),
                pago.getEstadoPago(),
                pago.getFechaPago()
        );
    }
}
