package sv.edu.ues.reservas.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sv.edu.ues.reservas.dto.horariorestaurante.HorarioRestauranteActualizarRequest;
import sv.edu.ues.reservas.dto.horariorestaurante.HorarioRestauranteCrearRequest;
import sv.edu.ues.reservas.dto.horariorestaurante.HorarioRestauranteResponse;
import sv.edu.ues.reservas.service.HorarioRestauranteService;

import java.util.List;
import java.util.UUID;

@Tag(name = "Horarios", description = "Horarios de atención de las sucursales")
@RestController
@RequestMapping("/api/v1/horarios")
@RequiredArgsConstructor
public class HorarioRestauranteController {

    private final HorarioRestauranteService horarioRestauranteService;

    @PostMapping
    public ResponseEntity<HorarioRestauranteResponse> crear(
            @Valid @RequestBody HorarioRestauranteCrearRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(horarioRestauranteService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<HorarioRestauranteResponse>> obtenerTodos() {
        return ResponseEntity.ok(
                horarioRestauranteService.obtenerTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioRestauranteResponse> obtenerPorId(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                horarioRestauranteService.obtenerPorId(id)
        );
    }

    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<List<HorarioRestauranteResponse>> obtenerPorSucursal(
            @PathVariable UUID idSucursal
    ) {
        return ResponseEntity.ok(
                horarioRestauranteService.obtenerPorSucursal(idSucursal)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioRestauranteResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody HorarioRestauranteActualizarRequest request
    ) {
        return ResponseEntity.ok(
                horarioRestauranteService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id
    ) {
        horarioRestauranteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}