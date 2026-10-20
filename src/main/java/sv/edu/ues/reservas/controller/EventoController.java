package sv.edu.ues.reservas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sv.edu.ues.reservas.dto.evento.EventoActualizarRequest;
import sv.edu.ues.reservas.dto.evento.EventoCrearRequest;
import sv.edu.ues.reservas.dto.evento.EventoResponse;
import sv.edu.ues.reservas.service.EventoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PostMapping
    public ResponseEntity<EventoResponse> crear(@Valid @RequestBody EventoCrearRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<EventoResponse>> obtenerTodos() {
        return ResponseEntity.ok(eventoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(eventoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody EventoActualizarRequest request
    ) {
        return ResponseEntity.ok(eventoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        eventoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
