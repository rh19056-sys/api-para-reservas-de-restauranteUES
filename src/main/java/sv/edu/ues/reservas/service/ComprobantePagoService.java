package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sv.edu.ues.reservas.domain.entity.ComprobantePago;
import sv.edu.ues.reservas.domain.entity.Pago;
import sv.edu.ues.reservas.domain.enums.FormatoComprobante;
import sv.edu.ues.reservas.dto.comprobante.ComprobantePagoCrearRequest;
import sv.edu.ues.reservas.dto.comprobante.ComprobantePagoResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.ComprobantePagoRepository;
import sv.edu.ues.reservas.util.ComprobanteNumeroGenerator;
import sv.edu.ues.reservas.util.EnumParser;
import sv.edu.ues.reservas.validation.PagoValidator;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComprobantePagoService {

    private static final Logger logger = LoggerFactory.getLogger(ComprobantePagoService.class);

    private final ComprobantePagoRepository comprobantePagoRepository;
    private final PagoService pagoService;
    private final PagoValidator pagoValidator;
    private final ComprobanteNumeroGenerator numeroGenerator;

    @Transactional
    public ComprobantePagoResponse emitir(
            UUID idPago,
            ComprobantePagoCrearRequest request
    ) {

        Pago pago = pagoService.buscarEntidad(idPago);

        pagoValidator.validarEmisionComprobante(pago);

        if (comprobantePagoRepository.findByPagoIdPago(idPago).isPresent()) {
            throw new IllegalArgumentException(
                    "El pago ya posee un comprobante"
            );
        }

        ComprobantePago comprobante = ComprobantePago.builder()
                .pago(pago)
                .numeroComprobante(resolverNumero(request.numeroComprobante()))
                .formato(
                        EnumParser.parse(
                                FormatoComprobante.class,
                                request.formato(),
                                "Formato de comprobante"
                        )
                )
                .referencia(request.referencia())
                .desglose(request.desglose())
                .build();

        ComprobantePago guardado = comprobantePagoRepository.save(comprobante);
        logger.info(
                "Comprobante emitido. idPago={}, numero={}",
                idPago, guardado.getNumeroComprobante()
        );

        return toResponse(guardado);
    }

    @Transactional
    public ComprobantePagoResponse obtenerPorPago(UUID idPago) {

        return toResponse(buscarPorPago(idPago));
    }

    @Transactional
    public void eliminar(UUID idPago) {

        comprobantePagoRepository.delete(buscarPorPago(idPago));
        logger.info("Comprobante eliminado. idPago={}", idPago);
    }

    private ComprobantePago buscarPorPago(UUID idPago) {

        return comprobantePagoRepository.findByPagoIdPago(idPago)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "El pago no tiene comprobante: " + idPago
                        )
                );
    }

    // Usa el número enviado (si no está repetido) o genera uno.
    private String resolverNumero(String solicitado) {

        if (!StringUtils.hasText(solicitado)) {
            return generarNumero();
        }

        String numero = solicitado.trim();

        if (comprobantePagoRepository.existsByNumeroComprobante(numero)) {
            throw new IllegalArgumentException(
                    "Número de comprobante duplicado: " + numero
            );
        }

        return numero;
    }

    // Reintenta hasta obtener un número que no exista en BD.
    private String generarNumero() {

        String numero;

        do {
            numero = numeroGenerator.generar();

        } while (comprobantePagoRepository.existsByNumeroComprobante(numero));

        return numero;
    }

    private ComprobantePagoResponse toResponse(ComprobantePago comprobante) {

        return new ComprobantePagoResponse(
                comprobante.getIdComprobante(),
                comprobante.getPago().getIdPago(),
                comprobante.getNumeroComprobante(),
                comprobante.getFechaEmision(),
                comprobante.getFormato(),
                comprobante.getReferencia(),
                comprobante.getDesglose()
        );
    }
}
