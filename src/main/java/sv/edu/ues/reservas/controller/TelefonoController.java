package sv.edu.ues.reservas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.ues.reservas.dto.telefono.TelefonoActualizarRequest;
import sv.edu.ues.reservas.dto.telefono.TelefonoCrearRequest;
import sv.edu.ues.reservas.dto.telefono.TelefonoResponse;
import sv.edu.ues.reservas.service.TelefonoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/telefonos")
@RequiredArgsConstructor
public class TelefonoController {

    private final TelefonoService telefonoService;

    @PostMapping
    public ResponseEntity<TelefonoResponse> crear(
            @Valid @RequestBody TelefonoCrearRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(telefonoService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<TelefonoResponse>> obtenerTodos() {

        return ResponseEntity.ok(
                telefonoService.obtenerTodos()
        );
    }

    @GetMapping("/{idTelefono}")
    public ResponseEntity<TelefonoResponse> obtenerPorId(
            @PathVariable UUID idTelefono
    ) {
        return ResponseEntity.ok(
                telefonoService.obtenerPorId(idTelefono)
        );
    }

    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<List<TelefonoResponse>> obtenerPorCuenta(
            @PathVariable UUID idCuenta
    ) {
        return ResponseEntity.ok(
                telefonoService.obtenerPorCuenta(idCuenta)
        );
    }

    @PutMapping("/{idTelefono}")
    public ResponseEntity<TelefonoResponse> actualizar(
            @PathVariable UUID idTelefono,
            @Valid @RequestBody TelefonoActualizarRequest request
    ) {
        return ResponseEntity.ok(
                telefonoService.actualizar(idTelefono, request)
        );
    }

    @DeleteMapping("/{idTelefono}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID idTelefono
    ) {
        telefonoService.eliminar(idTelefono);

        return ResponseEntity.noContent().build();
    }
}