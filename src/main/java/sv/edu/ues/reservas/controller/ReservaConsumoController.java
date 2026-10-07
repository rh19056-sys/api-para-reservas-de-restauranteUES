package sv.edu.ues.reservas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.ues.reservas.dto.consumo.ConsumoActualizarRequest;
import sv.edu.ues.reservas.dto.consumo.ConsumoCrearRequest;
import sv.edu.ues.reservas.dto.consumo.ConsumoResponse;
import sv.edu.ues.reservas.service.ReservaBebidaService;
import sv.edu.ues.reservas.service.ReservaMenuService;
import sv.edu.ues.reservas.service.ReservaPlatoService;

import java.util.List;
import java.util.UUID;

// Platos, menús y bebidas consumidos dentro de una reserva.
// El CRUD de la reserva en sí pertenece a ReservaController (integrante 4).
@RestController
@RequestMapping("/api/v1/reservas/{idReserva}")
@RequiredArgsConstructor
public class ReservaConsumoController {

    private final ReservaPlatoService reservaPlatoService;
    private final ReservaMenuService reservaMenuService;
    private final ReservaBebidaService reservaBebidaService;

    // ================= PLATOS =================

    @PostMapping("/platos")
    public ResponseEntity<ConsumoResponse> agregarPlato(
            @PathVariable UUID idReserva,
            @Valid @RequestBody ConsumoCrearRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservaPlatoService.agregar(idReserva, request));
    }

    @GetMapping("/platos")
    public ResponseEntity<List<ConsumoResponse>> obtenerPlatos(
            @PathVariable UUID idReserva
    ) {
        return ResponseEntity.ok(
                reservaPlatoService.obtenerPorReserva(idReserva)
        );
    }

    @PutMapping("/platos/{idPlato}")
    public ResponseEntity<ConsumoResponse> actualizarPlato(
            @PathVariable UUID idReserva,
            @PathVariable UUID idPlato,
            @Valid @RequestBody ConsumoActualizarRequest request
    ) {
        return ResponseEntity.ok(
                reservaPlatoService.actualizar(idReserva, idPlato, request)
        );
    }

    @DeleteMapping("/platos/{idPlato}")
    public ResponseEntity<Void> eliminarPlato(
            @PathVariable UUID idReserva,
            @PathVariable UUID idPlato
    ) {
        reservaPlatoService.eliminar(idReserva, idPlato);

        return ResponseEntity.noContent().build();
    }

    // ================= MENÚS =================

    @PostMapping("/menus")
    public ResponseEntity<ConsumoResponse> agregarMenu(
            @PathVariable UUID idReserva,
            @Valid @RequestBody ConsumoCrearRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservaMenuService.agregar(idReserva, request));
    }

    @GetMapping("/menus")
    public ResponseEntity<List<ConsumoResponse>> obtenerMenus(
            @PathVariable UUID idReserva
    ) {
        return ResponseEntity.ok(
                reservaMenuService.obtenerPorReserva(idReserva)
        );
    }

    @PutMapping("/menus/{idMenu}")
    public ResponseEntity<ConsumoResponse> actualizarMenu(
            @PathVariable UUID idReserva,
            @PathVariable UUID idMenu,
            @Valid @RequestBody ConsumoActualizarRequest request
    ) {
        return ResponseEntity.ok(
                reservaMenuService.actualizar(idReserva, idMenu, request)
        );
    }

    @DeleteMapping("/menus/{idMenu}")
    public ResponseEntity<Void> eliminarMenu(
            @PathVariable UUID idReserva,
            @PathVariable UUID idMenu
    ) {
        reservaMenuService.eliminar(idReserva, idMenu);

        return ResponseEntity.noContent().build();
    }

    // ================= BEBIDAS =================

    @PostMapping("/bebidas")
    public ResponseEntity<ConsumoResponse> agregarBebida(
            @PathVariable UUID idReserva,
            @Valid @RequestBody ConsumoCrearRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservaBebidaService.agregar(idReserva, request));
    }

    @GetMapping("/bebidas")
    public ResponseEntity<List<ConsumoResponse>> obtenerBebidas(
            @PathVariable UUID idReserva
    ) {
        return ResponseEntity.ok(
                reservaBebidaService.obtenerPorReserva(idReserva)
        );
    }

    @PutMapping("/bebidas/{idBebida}")
    public ResponseEntity<ConsumoResponse> actualizarBebida(
            @PathVariable UUID idReserva,
            @PathVariable UUID idBebida,
            @Valid @RequestBody ConsumoActualizarRequest request
    ) {
        return ResponseEntity.ok(
                reservaBebidaService.actualizar(idReserva, idBebida, request)
        );
    }

    @DeleteMapping("/bebidas/{idBebida}")
    public ResponseEntity<Void> eliminarBebida(
            @PathVariable UUID idReserva,
            @PathVariable UUID idBebida
    ) {
        reservaBebidaService.eliminar(idReserva, idBebida);

        return ResponseEntity.noContent().build();
    }
}
