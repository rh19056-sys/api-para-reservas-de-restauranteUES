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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sv.edu.ues.reservas.dto.catalogo.PlatoActualizarRequest;
import sv.edu.ues.reservas.dto.catalogo.PlatoCrearRequest;
import sv.edu.ues.reservas.dto.catalogo.PlatoResponse;
import sv.edu.ues.reservas.service.CatalogoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/platos")
@RequiredArgsConstructor
public class PlatoController {

    private final CatalogoService catalogoService;

    @PostMapping
    public ResponseEntity<PlatoResponse> crear(@Valid @RequestBody PlatoCrearRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearPlato(request));
    }

    @GetMapping
    public List<PlatoResponse> obtenerTodos(@RequestParam(required = false) UUID idSucursal) {
        return catalogoService.obtenerPlatos(idSucursal);
    }

    @GetMapping("/{id}")
    public PlatoResponse obtenerPorId(@PathVariable UUID id) {
        return catalogoService.obtenerPlato(id);
    }

    @PutMapping("/{id}")
    public PlatoResponse actualizar(@PathVariable UUID id,
                                    @Valid @RequestBody PlatoActualizarRequest request) {
        return catalogoService.actualizarPlato(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        catalogoService.eliminarPlato(id);
        return ResponseEntity.noContent().build();
    }
}
