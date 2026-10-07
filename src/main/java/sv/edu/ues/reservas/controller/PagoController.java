package sv.edu.ues.reservas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.ues.reservas.dto.comprobante.ComprobantePagoCrearRequest;
import sv.edu.ues.reservas.dto.comprobante.ComprobantePagoResponse;
import sv.edu.ues.reservas.dto.pago.PagoActualizarRequest;
import sv.edu.ues.reservas.dto.pago.PagoCrearRequest;
import sv.edu.ues.reservas.dto.pago.PagoResponse;
import sv.edu.ues.reservas.service.ComprobantePagoService;
import sv.edu.ues.reservas.service.PagoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;
    private final ComprobantePagoService comprobantePagoService;

    @PostMapping
    public ResponseEntity<PagoResponse> crear(
            @Valid @RequestBody PagoCrearRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pagoService.crear(request));
    }

    // GET /api/v1/pagos              -> todos
    // GET /api/v1/pagos?idReserva=.. -> pagos de una reserva
    @GetMapping
    public ResponseEntity<List<PagoResponse>> obtener(
            @RequestParam(required = false) UUID idReserva
    ) {

        if (idReserva != null) {
            return ResponseEntity.ok(
                    pagoService.obtenerPorReserva(idReserva)
            );
        }

        return ResponseEntity.ok(
                pagoService.obtenerTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> obtenerPorId(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                pagoService.obtenerPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody PagoActualizarRequest request
    ) {

        return ResponseEntity.ok(
                pagoService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id
    ) {

        pagoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    // Un pago tiene como máximo un comprobante (id_pago UNIQUE).
    @PostMapping("/{id}/comprobante")
    public ResponseEntity<ComprobantePagoResponse> emitirComprobante(
            @PathVariable UUID id,
            @Valid @RequestBody ComprobantePagoCrearRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(comprobantePagoService.emitir(id, request));
    }

    @GetMapping("/{id}/comprobante")
    public ResponseEntity<ComprobantePagoResponse> obtenerComprobante(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                comprobantePagoService.obtenerPorPago(id)
        );
    }

    @DeleteMapping("/{id}/comprobante")
    public ResponseEntity<Void> eliminarComprobante(
            @PathVariable UUID id
    ) {

        comprobantePagoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
