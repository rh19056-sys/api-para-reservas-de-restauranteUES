package sv.edu.ues.reservas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.ues.reservas.dto.restaurantesucursal.RestauranteSucursalActualizarRequest;
import sv.edu.ues.reservas.dto.restaurantesucursal.RestauranteSucursalCrearRequest;
import sv.edu.ues.reservas.dto.restaurantesucursal.RestauranteSucursalResponse;
import sv.edu.ues.reservas.service.RestauranteSucursalService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sucursales")
@RequiredArgsConstructor
public class RestauranteSucursalController {

    private final RestauranteSucursalService restauranteSucursalService;

    @PostMapping
    public ResponseEntity<RestauranteSucursalResponse> crear(
            @Valid @RequestBody RestauranteSucursalCrearRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(restauranteSucursalService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<RestauranteSucursalResponse>> obtenerTodas() {

        return ResponseEntity.ok(
                restauranteSucursalService.obtenerTodas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteSucursalResponse> obtenerPorId(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                restauranteSucursalService.obtenerPorId(id)
        );
    }

    @GetMapping("/restaurante/{idRestauranteOrg}")
    public ResponseEntity<List<RestauranteSucursalResponse>>
            obtenerPorRestauranteOrg(
                    @PathVariable UUID idRestauranteOrg
            ) {

        return ResponseEntity.ok(
                restauranteSucursalService
                        .obtenerPorRestauranteOrg(idRestauranteOrg)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestauranteSucursalResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody RestauranteSucursalActualizarRequest request
    ) {

        return ResponseEntity.ok(
                restauranteSucursalService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id
    ) {

        restauranteSucursalService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}