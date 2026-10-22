package sv.edu.ues.reservas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.ues.reservas.dto.perfilgestor.PerfilGestorActualizarRequest;
import sv.edu.ues.reservas.dto.perfilgestor.PerfilGestorCrearRequest;
import sv.edu.ues.reservas.dto.perfilgestor.PerfilGestorResponse;
import sv.edu.ues.reservas.service.PerfilGestorService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/perfiles-gestores")
@RequiredArgsConstructor
public class PerfilGestorController {

    private final PerfilGestorService perfilGestorService;

    @PostMapping("/{idCuenta}")
    public ResponseEntity<PerfilGestorResponse> crear(
            @PathVariable UUID idCuenta,
            @Valid @RequestBody PerfilGestorCrearRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(perfilGestorService.crear(idCuenta, request));
    }

    @GetMapping
    public ResponseEntity<List<PerfilGestorResponse>> obtenerTodos() {

        return ResponseEntity.ok(
                perfilGestorService.obtenerTodos()
        );
    }

    @GetMapping("/{idCuenta}")
    public ResponseEntity<PerfilGestorResponse> obtenerPorId(
            @PathVariable UUID idCuenta
    ) {

        return ResponseEntity.ok(
                perfilGestorService.obtenerPorId(idCuenta)
        );
    }

    @PutMapping("/{idCuenta}")
    public ResponseEntity<PerfilGestorResponse> actualizar(
            @PathVariable UUID idCuenta,
            @Valid @RequestBody PerfilGestorActualizarRequest request
    ) {

        return ResponseEntity.ok(
                perfilGestorService.actualizar(idCuenta, request)
        );
    }

    @DeleteMapping("/{idCuenta}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID idCuenta
    ) {

        perfilGestorService.eliminar(idCuenta);

        return ResponseEntity.noContent().build();
    }
}