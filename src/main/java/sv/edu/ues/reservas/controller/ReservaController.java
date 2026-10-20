package sv.edu.ues.reservas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sv.edu.ues.reservas.dto.reserva.ReservaActualizarRequest;
import sv.edu.ues.reservas.dto.reserva.ReservaCrearRequest;
import sv.edu.ues.reservas.dto.reserva.ReservaResponse;
import sv.edu.ues.reservas.service.ReservaService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponse> crear(@Valid @RequestBody ReservaCrearRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> obtenerTodas(
            @RequestParam(required = false) UUID idCuentaCliente,
            @RequestParam(required = false) UUID idSucursal
    ) {
        if (idCuentaCliente != null) {
            return ResponseEntity.ok(reservaService.obtenerPorCliente(idCuentaCliente));
        }
        if (idSucursal != null) {
            return ResponseEntity.ok(reservaService.obtenerPorSucursal(idSucursal));
        }
        return ResponseEntity.ok(reservaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ReservaActualizarRequest request
    ) {
        return ResponseEntity.ok(reservaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
