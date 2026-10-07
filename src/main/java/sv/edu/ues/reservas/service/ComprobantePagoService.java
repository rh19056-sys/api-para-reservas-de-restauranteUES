package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.ComprobantePago;
import sv.edu.ues.reservas.domain.entity.Pago;
import sv.edu.ues.reservas.domain.enums.EstadoPago;
import sv.edu.ues.reservas.domain.enums.FormatoComprobante;
import sv.edu.ues.reservas.dto.comprobante.ComprobantePagoCrearRequest;
import sv.edu.ues.reservas.dto.comprobante.ComprobantePagoResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.ComprobantePagoRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComprobantePagoService {

    private final ComprobantePagoRepository comprobantePagoRepository;
    private final PagoService pagoService;

    @Transactional
    public ComprobantePagoResponse emitir(
            UUID idPago,
            ComprobantePagoCrearRequest request
    ) {

        Pago pago = pagoService.buscarEntidad(idPago);

        if (pago.getEstadoPago() != EstadoPago.APROBADO) {
            throw new IllegalArgumentException(
                    "Solo se emite comprobante para pagos APROBADOS"
            );
        }

        if (comprobantePagoRepository.findByPagoIdPago(idPago).isPresent()) {
            throw new IllegalArgumentException(
                    "El pago ya posee un comprobante"
            );
        }

        String numero = request.numeroComprobante();

        if (numero == null || numero.isBlank()) {
            numero = generarNumero();

        } else if (comprobantePagoRepository
                .existsByNumeroComprobante(numero.trim())) {

            throw new IllegalArgumentException(
                    "Número de comprobante duplicado: " + numero
            );
        }

        ComprobantePago comprobante = ComprobantePago.builder()
                .pago(pago)
                .numeroComprobante(numero.trim())
                .formato(parseFormato(request.formato()))
                .referencia(request.referencia())
                .desglose(request.desglose())
                .build();

        return toResponse(comprobantePagoRepository.save(comprobante));
    }

    @Transactional
    public ComprobantePagoResponse obtenerPorPago(UUID idPago) {

        return toResponse(buscarPorPago(idPago));
    }

    @Transactional
    public void eliminar(UUID idPago) {

        comprobantePagoRepository.delete(buscarPorPago(idPago));
    }

    private ComprobantePago buscarPorPago(UUID idPago) {

        return comprobantePagoRepository.findByPagoIdPago(idPago)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "El pago no tiene comprobante: " + idPago
                        )
                );
    }

    // Formato: CP-yyyyMMdd-XXXXXXXX
    private String generarNumero() {

        String numero;

        do {
            numero = "CP-"
                    + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                    + "-"
                    + UUID.randomUUID().toString()
                    .substring(0, 8)
                    .toUpperCase();

        } while (comprobantePagoRepository.existsByNumeroComprobante(numero));

        return numero;
    }

    private FormatoComprobante parseFormato(String valor) {

        try {
            return FormatoComprobante.valueOf(
                    valor.trim().toUpperCase()
            );

        } catch (IllegalArgumentException ex) {

            throw new IllegalArgumentException(
                    "Formato de comprobante inválido: " + valor
            );
        }
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
