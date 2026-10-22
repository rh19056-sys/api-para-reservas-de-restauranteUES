package sv.edu.ues.reservas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.ues.reservas.dto.preferenciacliente.PreferenciaClienteActualizarRequest;
import sv.edu.ues.reservas.dto.preferenciacliente.PreferenciaClienteCrearRequest;
import sv.edu.ues.reservas.dto.preferenciacliente.PreferenciaClienteResponse;
import sv.edu.ues.reservas.service.PreferenciaClienteService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/preferencias-cliente")
@RequiredArgsConstructor
public class PreferenciaClienteController {

    private final PreferenciaClienteService preferenciaClienteService;

    @PostMapping
    public ResponseEntity<PreferenciaClienteResponse> crear(
            @Valid @RequestBody PreferenciaClienteCrearRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(preferenciaClienteService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<PreferenciaClienteResponse>> obtenerTodas() {

        return ResponseEntity.ok(
                preferenciaClienteService.obtenerTodas());
    }

    @GetMapping("/{idPreferencia}")
    public ResponseEntity<PreferenciaClienteResponse> obtenerPorId(
            @PathVariable UUID idPreferencia) {

        return ResponseEntity.ok(
                preferenciaClienteService.obtenerPorId(idPreferencia));
    }

    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<List<PreferenciaClienteResponse>> obtenerPorCuenta(
            @PathVariable UUID idCuenta) {

        return ResponseEntity.ok(
                preferenciaClienteService.obtenerPorCuenta(idCuenta));
    }

    @PutMapping("/{idPreferencia}")
    public ResponseEntity<PreferenciaClienteResponse> actualizar(
            @PathVariable UUID idPreferencia,
            @Valid @RequestBody PreferenciaClienteActualizarRequest request) {

        return ResponseEntity.ok(
                preferenciaClienteService.actualizar(idPreferencia, request));
    }

    @DeleteMapping("/{idPreferencia}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID idPreferencia) {

        preferenciaClienteService.eliminar(idPreferencia);

        return ResponseEntity.noContent().build();
    }
}