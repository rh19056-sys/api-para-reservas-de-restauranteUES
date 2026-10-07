package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.Cuenta;
import sv.edu.ues.reservas.domain.entity.PerfilCliente;
import sv.edu.ues.reservas.dto.perfilcliente.PerfilClienteActualizarRequest;
import sv.edu.ues.reservas.dto.perfilcliente.PerfilClienteCrearRequest;
import sv.edu.ues.reservas.dto.perfilcliente.PerfilClienteResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.CuentaRepository;
import sv.edu.ues.reservas.repository.PerfilClienteRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerfilClienteService {

    private final PerfilClienteRepository perfilClienteRepository;
    private final CuentaRepository cuentaRepository;

    @Transactional
    public PerfilClienteResponse crear(
            UUID idCuenta,
            PerfilClienteCrearRequest request
    ) {

        Cuenta cuenta = cuentaRepository.findById(idCuenta)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Cuenta no encontrada: " + idCuenta
                        )
                );

        if (perfilClienteRepository.existsById(idCuenta)) {
            throw new IllegalArgumentException(
                    "La cuenta ya posee un perfil de cliente"
            );
        }

        PerfilCliente perfilCliente = PerfilCliente.builder()
                .idCuenta(idCuenta)
                .cuenta(cuenta)
                .nombre(request.nombre())
                .build();

        PerfilCliente guardado =
                perfilClienteRepository.save(perfilCliente);

        return toResponse(guardado);
    }

    @Transactional
    public List<PerfilClienteResponse> obtenerTodos() {

        return perfilClienteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PerfilClienteResponse obtenerPorId(
            UUID idCuenta
    ) {

        return toResponse(
                buscarEntidad(idCuenta)
        );
    }

    @Transactional
    public PerfilClienteResponse actualizar(
            UUID idCuenta,
            PerfilClienteActualizarRequest request
    ) {

        PerfilCliente perfilCliente =
                buscarEntidad(idCuenta);

        if (request.nombre() != null
                && !request.nombre().isBlank()) {

            perfilCliente.setNombre(
                    request.nombre().trim()
            );
        }

        PerfilCliente actualizado =
                perfilClienteRepository.save(perfilCliente);

        return toResponse(actualizado);
    }

    @Transactional
    public void eliminar(UUID idCuenta) {

        PerfilCliente perfilCliente =
                buscarEntidad(idCuenta);

        perfilClienteRepository.delete(perfilCliente);
    }

    private PerfilCliente buscarEntidad(UUID idCuenta) {

        return perfilClienteRepository.findById(idCuenta)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Perfil de cliente no encontrado: "
                                        + idCuenta
                        )
                );
    }

    private PerfilClienteResponse toResponse(
            PerfilCliente perfilCliente
    ) {

        return new PerfilClienteResponse(
                perfilCliente.getIdCuenta(),
                perfilCliente.getNombre()
        );
    }
}