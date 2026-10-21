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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sv.edu.ues.reservas.dto.catalogo.MenuActualizarRequest;
import sv.edu.ues.reservas.dto.catalogo.MenuBebidaRequest;
import sv.edu.ues.reservas.dto.catalogo.MenuBebidaResponse;
import sv.edu.ues.reservas.dto.catalogo.MenuCrearRequest;
import sv.edu.ues.reservas.dto.catalogo.MenuPlatoRequest;
import sv.edu.ues.reservas.dto.catalogo.MenuPlatoResponse;
import sv.edu.ues.reservas.dto.catalogo.MenuResponse;
import sv.edu.ues.reservas.service.CatalogoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {

    private final CatalogoService catalogoService;

    @PostMapping
    public ResponseEntity<MenuResponse> crear(@Valid @RequestBody MenuCrearRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearMenu(request));
    }

    @GetMapping
    public List<MenuResponse> obtenerTodos(@RequestParam(required = false) UUID idSucursal) {
        return catalogoService.obtenerMenus(idSucursal);
    }

    @GetMapping("/{id}")
    public MenuResponse obtenerPorId(@PathVariable UUID id) {
        return catalogoService.obtenerMenu(id);
    }

    @PutMapping("/{id}")
    public MenuResponse actualizar(@PathVariable UUID id,
                                   @Valid @RequestBody MenuActualizarRequest request) {
        return catalogoService.actualizarMenu(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        catalogoService.eliminarMenu(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/platos")
    public List<MenuPlatoResponse> obtenerPlatos(@PathVariable UUID id) {
        return catalogoService.obtenerPlatosMenu(id);
    }

    @PostMapping("/{id}/platos")
    public ResponseEntity<MenuPlatoResponse> asignarPlato(
            @PathVariable UUID id, @Valid @RequestBody MenuPlatoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.asignarPlato(id, request));
    }

    @PutMapping("/{id}/platos/{idPlato}")
    public MenuPlatoResponse actualizarPlato(
            @PathVariable UUID id, @PathVariable UUID idPlato,
            @Valid @RequestBody MenuPlatoRequest request) {
        return catalogoService.actualizarPlatoMenu(id, idPlato, request);
    }

    @DeleteMapping("/{id}/platos/{idPlato}")
    public ResponseEntity<Void> quitarPlato(@PathVariable UUID id, @PathVariable UUID idPlato) {
        catalogoService.quitarPlatoMenu(id, idPlato);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/bebidas")
    public List<MenuBebidaResponse> obtenerBebidas(@PathVariable UUID id) {
        return catalogoService.obtenerBebidasMenu(id);
    }

    @PostMapping("/{id}/bebidas")
    public ResponseEntity<MenuBebidaResponse> asignarBebida(
            @PathVariable UUID id, @Valid @RequestBody MenuBebidaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.asignarBebida(id, request));
    }

    @PutMapping("/{id}/bebidas/{idBebida}")
    public MenuBebidaResponse actualizarBebida(
            @PathVariable UUID id, @PathVariable UUID idBebida,
            @Valid @RequestBody MenuBebidaRequest request) {
        return catalogoService.actualizarBebidaMenu(id, idBebida, request);
    }

    @DeleteMapping("/{id}/bebidas/{idBebida}")
    public ResponseEntity<Void> quitarBebida(@PathVariable UUID id, @PathVariable UUID idBebida) {
        catalogoService.quitarBebidaMenu(id, idBebida);
        return ResponseEntity.noContent().build();
    }
}
