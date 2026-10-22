package sv.edu.ues.reservas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.ues.reservas.dto.correo.CorreoActualizarRequest;
import sv.edu.ues.reservas.dto.correo.CorreoCrearRequest;
import sv.edu.ues.reservas.dto.correo.CorreoResponse;
import sv.edu.ues.reservas.service.CorreoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/correos")
@RequiredArgsConstructor
public class CorreoController {

    private final CorreoService correoService;

    @PostMapping
    public ResponseEntity<CorreoResponse> crear(
            @Valid @RequestBody CorreoCrearRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(correoService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<CorreoResponse>> obtenerTodos() {

        return ResponseEntity.ok(
                correoService.obtenerTodos()
        );
    }

    @GetMapping("/{idCorreo}")
    public ResponseEntity<CorreoResponse> obtenerPorId(
            @PathVariable UUID idCorreo
    ) {
        return ResponseEntity.ok(
                correoService.obtenerPorId(idCorreo)
        );
    }

    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<List<CorreoResponse>> obtenerPorCuenta(
            @PathVariable UUID idCuenta
    ) {
        return ResponseEntity.ok(
                correoService.obtenerPorCuenta(idCuenta)
        );
    }

    @PutMapping("/{idCorreo}")
    public ResponseEntity<CorreoResponse> actualizar(
            @PathVariable UUID idCorreo,
            @Valid @RequestBody CorreoActualizarRequest request
    ) {
        return ResponseEntity.ok(
                correoService.actualizar(idCorreo, request)
        );
    }

    @DeleteMapping("/{idCorreo}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID idCorreo
    ) {
        correoService.eliminar(idCorreo);

        return ResponseEntity.noContent().build();
    }
}