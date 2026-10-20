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
import sv.edu.ues.reservas.dto.catalogo.BebidaActualizarRequest;
import sv.edu.ues.reservas.dto.catalogo.BebidaCrearRequest;
import sv.edu.ues.reservas.dto.catalogo.BebidaResponse;
import sv.edu.ues.reservas.dto.catalogo.VinoActualizarRequest;
import sv.edu.ues.reservas.dto.catalogo.VinoCrearRequest;
import sv.edu.ues.reservas.dto.catalogo.VinoResponse;
import sv.edu.ues.reservas.service.CatalogoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bebidas")
@RequiredArgsConstructor
public class BebidaController {

    private final CatalogoService catalogoService;

    @PostMapping
    public ResponseEntity<BebidaResponse> crear(@Valid @RequestBody BebidaCrearRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearBebida(request));
    }

    @GetMapping
    public List<BebidaResponse> obtenerTodas(@RequestParam(required = false) UUID idSucursal) {
        return catalogoService.obtenerBebidas(idSucursal);
    }

    @GetMapping("/{id}")
    public BebidaResponse obtenerPorId(@PathVariable UUID id) {
        return catalogoService.obtenerBebida(id);
    }

    @PutMapping("/{id}")
    public BebidaResponse actualizar(@PathVariable UUID id,
                                     @Valid @RequestBody BebidaActualizarRequest request) {
        return catalogoService.actualizarBebida(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        catalogoService.eliminarBebida(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/vinos")
    public ResponseEntity<VinoResponse> crearVino(@Valid @RequestBody VinoCrearRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearVino(request));
    }

    @GetMapping("/vinos")
    public List<VinoResponse> obtenerVinos(@RequestParam(required = false) UUID idSucursal) {
        return catalogoService.obtenerVinos(idSucursal);
    }

    @GetMapping("/{id}/vino")
    public VinoResponse obtenerVino(@PathVariable UUID id) {
        return catalogoService.obtenerVino(id);
    }

    @PutMapping("/{id}/vino")
    public VinoResponse actualizarVino(@PathVariable UUID id,
                                       @Valid @RequestBody VinoActualizarRequest request) {
        return catalogoService.actualizarVino(id, request);
    }

    @DeleteMapping("/{id}/vino")
    public ResponseEntity<Void> eliminarVino(@PathVariable UUID id) {
        catalogoService.eliminarVino(id);
        return ResponseEntity.noContent().build();
    }
}
