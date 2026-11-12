package sv.edu.ues.reservas.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sv.edu.ues.reservas.dto.politicareserva.PoliticaReservaActualizarRequest;
import sv.edu.ues.reservas.dto.politicareserva.PoliticaReservaCrearRequest;
import sv.edu.ues.reservas.dto.politicareserva.PoliticaReservaResponse;
import sv.edu.ues.reservas.service.PoliticaReservaService;

import java.util.List;
import java.util.UUID;

@Tag(name = "Políticas de reserva", description = "Reglas y políticas de reserva por sucursal")
@RestController
@RequestMapping("/api/v1/politicas-reserva")
@RequiredArgsConstructor
public class PoliticaReservaController {

    private final PoliticaReservaService politicaReservaService;

    @PostMapping
    public ResponseEntity<PoliticaReservaResponse> crear(
            @Valid @RequestBody PoliticaReservaCrearRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(politicaReservaService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<PoliticaReservaResponse>> obtenerTodas() {
        return ResponseEntity.ok(
                politicaReservaService.obtenerTodas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PoliticaReservaResponse> obtenerPorId(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                politicaReservaService.obtenerPorId(id)
        );
    }

    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<List<PoliticaReservaResponse>> obtenerPorSucursal(
            @PathVariable UUID idSucursal
    ) {
        return ResponseEntity.ok(
                politicaReservaService.obtenerPorSucursal(idSucursal)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PoliticaReservaResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody PoliticaReservaActualizarRequest request
    ) {
        return ResponseEntity.ok(
                politicaReservaService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id
    ) {
        politicaReservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}