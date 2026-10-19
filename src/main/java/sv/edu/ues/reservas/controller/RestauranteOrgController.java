package sv.edu.ues.reservas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sv.edu.ues.reservas.dto.restaurante.RestauranteOrgActualizarRequest;
import sv.edu.ues.reservas.dto.restaurante.RestauranteOrgCrearRequest;
import sv.edu.ues.reservas.dto.restaurante.RestauranteOrgResponse;
import sv.edu.ues.reservas.service.RestauranteOrgService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/restaurantes")
@RequiredArgsConstructor
public class RestauranteOrgController {

    private final RestauranteOrgService restauranteOrgService;

    @PostMapping
    public ResponseEntity<RestauranteOrgResponse> crear(
            @Valid @RequestBody RestauranteOrgCrearRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(restauranteOrgService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<RestauranteOrgResponse>> obtenerTodas() {
        return ResponseEntity.ok(restauranteOrgService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteOrgResponse> obtenerPorId(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(restauranteOrgService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestauranteOrgResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody RestauranteOrgActualizarRequest request
    ) {
        return ResponseEntity.ok(
                restauranteOrgService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        restauranteOrgService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}