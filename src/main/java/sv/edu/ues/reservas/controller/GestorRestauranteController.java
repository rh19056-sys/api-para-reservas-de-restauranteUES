package sv.edu.ues.reservas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sv.edu.ues.reservas.dto.gestorrestaurante.GestorRestauranteActualizarRequest;
import sv.edu.ues.reservas.dto.gestorrestaurante.GestorRestauranteCrearRequest;
import sv.edu.ues.reservas.dto.gestorrestaurante.GestorRestauranteResponse;
import sv.edu.ues.reservas.service.GestorRestauranteService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/gestores-restaurante")
@RequiredArgsConstructor
public class GestorRestauranteController {

    private final GestorRestauranteService gestorRestauranteService;

    @PostMapping
    public ResponseEntity<GestorRestauranteResponse> crear(
            @Valid @RequestBody GestorRestauranteCrearRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(gestorRestauranteService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<GestorRestauranteResponse>> obtenerTodos() {
        return ResponseEntity.ok(
                gestorRestauranteService.obtenerTodos()
        );
    }

    @GetMapping("/{idCuenta}/{idSucursal}")
    public ResponseEntity<GestorRestauranteResponse> obtenerPorId(
            @PathVariable UUID idCuenta,
            @PathVariable UUID idSucursal
    ) {
        return ResponseEntity.ok(
                gestorRestauranteService.obtenerPorId(idCuenta, idSucursal)
        );
    }

    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<List<GestorRestauranteResponse>> obtenerPorSucursal(
            @PathVariable UUID idSucursal
    ) {
        return ResponseEntity.ok(
                gestorRestauranteService.obtenerPorSucursal(idSucursal)
        );
    }

    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<List<GestorRestauranteResponse>> obtenerPorCuenta(
            @PathVariable UUID idCuenta
    ) {
        return ResponseEntity.ok(
                gestorRestauranteService.obtenerPorCuenta(idCuenta)
        );
    }

    @PutMapping("/{idCuenta}/{idSucursal}")
    public ResponseEntity<GestorRestauranteResponse> actualizar(
            @PathVariable UUID idCuenta,
            @PathVariable UUID idSucursal,
            @Valid @RequestBody GestorRestauranteActualizarRequest request
    ) {
        return ResponseEntity.ok(
                gestorRestauranteService.actualizar(
                        idCuenta,
                        idSucursal,
                        request
                )
        );
    }

    @DeleteMapping("/{idCuenta}/{idSucursal}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID idCuenta,
            @PathVariable UUID idSucursal
    ) {
        gestorRestauranteService.eliminar(idCuenta, idSucursal);
        return ResponseEntity.noContent().build();
    }
}