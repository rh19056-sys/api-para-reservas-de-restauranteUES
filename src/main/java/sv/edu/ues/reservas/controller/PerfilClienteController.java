package sv.edu.ues.reservas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.ues.reservas.dto.perfilcliente.PerfilClienteActualizarRequest;
import sv.edu.ues.reservas.dto.perfilcliente.PerfilClienteCrearRequest;
import sv.edu.ues.reservas.dto.perfilcliente.PerfilClienteResponse;
import sv.edu.ues.reservas.service.PerfilClienteService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/perfiles-clientes")
@RequiredArgsConstructor
public class PerfilClienteController {

    private final PerfilClienteService perfilClienteService;

    @PostMapping("/{idCuenta}")
    public ResponseEntity<PerfilClienteResponse> crear(
            @PathVariable UUID idCuenta,
            @Valid @RequestBody PerfilClienteCrearRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        perfilClienteService.crear(
                                idCuenta,
                                request
                        )
                );
    }
//Para crear el perfil utilizamos:
//POST /api/v1/perfiles-clientes/{idCuenta} 
//porque id_cuenta no se genera al crear el perfil, sino que ya existe en la tabla cuenta.
    @GetMapping
    public ResponseEntity<List<PerfilClienteResponse>> obtenerTodos() {

        return ResponseEntity.ok(
                perfilClienteService.obtenerTodos()
        );
    }

    @GetMapping("/{idCuenta}")
    public ResponseEntity<PerfilClienteResponse> obtenerPorId(
            @PathVariable UUID idCuenta
    ) {

        return ResponseEntity.ok(
                perfilClienteService.obtenerPorId(idCuenta)
        );
    }

    @PutMapping("/{idCuenta}")
    public ResponseEntity<PerfilClienteResponse> actualizar(
            @PathVariable UUID idCuenta,
            @Valid @RequestBody
            PerfilClienteActualizarRequest request
    ) {

        return ResponseEntity.ok(
                perfilClienteService.actualizar(
                        idCuenta,
                        request
                )
        );
    }

    @DeleteMapping("/{idCuenta}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID idCuenta
    ) {

        perfilClienteService.eliminar(idCuenta);

        return ResponseEntity.noContent().build();
    }
}