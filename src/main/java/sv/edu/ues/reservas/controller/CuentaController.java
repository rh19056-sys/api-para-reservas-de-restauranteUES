package sv.edu.ues.reservas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.ues.reservas.dto.cuenta.CuentaActualizarRequest;
import sv.edu.ues.reservas.dto.cuenta.CuentaCrearRequest;
import sv.edu.ues.reservas.dto.cuenta.CuentaResponse;
import sv.edu.ues.reservas.service.CuentaService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<CuentaResponse> crear(
            @Valid @RequestBody CuentaCrearRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cuentaService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> obtenerTodas() {

        return ResponseEntity.ok(
                cuentaService.obtenerTodas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponse> obtenerPorId(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                cuentaService.obtenerPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody CuentaActualizarRequest request
    ) {

        return ResponseEntity.ok(
                cuentaService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id
    ) {

        cuentaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}