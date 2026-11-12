package sv.edu.ues.reservas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sv.edu.ues.reservas.dto.mesa.MesaCrearRequest;
import sv.edu.ues.reservas.dto.mesa.MesaActualizarRequest;
import sv.edu.ues.reservas.dto.mesa.MesaResponse;

import sv.edu.ues.reservas.service.MesaService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mesas")
@RequiredArgsConstructor
public class MesaController {

    private final MesaService mesaService;

    // CREAR MESA
    @PostMapping
    public ResponseEntity<MesaResponse> crear(
            @Valid @RequestBody MesaCrearRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mesaService.crear(request));
    }

    // OBTENER TODAS LAS MESAS
    @GetMapping
    public ResponseEntity<List<MesaResponse>> obtenerTodas() {
        return ResponseEntity.ok(
                mesaService.obtenerTodas()
        );
    }

    // OBTENER MESA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<MesaResponse> obtenerPorId(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                mesaService.obtenerPorId(id)
        );
    }

    // OBTENER MESAS POR SUCURSAL
    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<List<MesaResponse>> obtenerPorSucursal(
            @PathVariable UUID idSucursal
    ) {
        return ResponseEntity.ok(
                mesaService.obtenerPorSucursal(idSucursal)
        );
    }

    // ACTUALIZAR MESA
    @PutMapping("/{id}")
    public ResponseEntity<MesaResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody MesaActualizarRequest request
    ) {
        return ResponseEntity.ok(
                mesaService.actualizar(id, request)
        );
    }

    // ELIMINAR MESA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id
    ) {
        mesaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}