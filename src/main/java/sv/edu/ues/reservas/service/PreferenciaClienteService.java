package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.PerfilCliente;
import sv.edu.ues.reservas.domain.entity.PreferenciaCliente;
import sv.edu.ues.reservas.dto.preferenciacliente.PreferenciaClienteActualizarRequest;
import sv.edu.ues.reservas.dto.preferenciacliente.PreferenciaClienteCrearRequest;
import sv.edu.ues.reservas.dto.preferenciacliente.PreferenciaClienteResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.PerfilClienteRepository;
import sv.edu.ues.reservas.repository.PreferenciaClienteRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PreferenciaClienteService {

    private final PreferenciaClienteRepository preferenciaClienteRepository;
    private final PerfilClienteRepository perfilClienteRepository;

    @Transactional
    public PreferenciaClienteResponse crear(
            PreferenciaClienteCrearRequest request) {

        PerfilCliente perfilCliente = perfilClienteRepository
                .findById(request.idCuenta())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Perfil de cliente no encontrado: "
                                        + request.idCuenta()));

        PreferenciaCliente preferencia = PreferenciaCliente.builder()
                .perfilCliente(perfilCliente)
                .tipo(request.tipo().trim())
                .detalle(request.detalle().trim())
                .build();

        PreferenciaCliente guardada =
                preferenciaClienteRepository.save(preferencia);

        return toResponse(guardada);
    }

    @Transactional
    public List<PreferenciaClienteResponse> obtenerTodas() {

        return preferenciaClienteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PreferenciaClienteResponse obtenerPorId(UUID idPreferencia) {

        return toResponse(buscarEntidad(idPreferencia));
    }

    @Transactional
    public List<PreferenciaClienteResponse> obtenerPorCuenta(UUID idCuenta) {

        if (!perfilClienteRepository.existsById(idCuenta)) {
            throw new RecursoNoEncontradoException(
                    "Perfil de cliente no encontrado: " + idCuenta);
        }

        return preferenciaClienteRepository
                .findByPerfilCliente_IdCuenta(idCuenta)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PreferenciaClienteResponse actualizar(
            UUID idPreferencia,
            PreferenciaClienteActualizarRequest request) {

        PreferenciaCliente preferencia = buscarEntidad(idPreferencia);

        preferencia.setTipo(request.tipo().trim());
        preferencia.setDetalle(request.detalle().trim());

        PreferenciaCliente actualizada =
                preferenciaClienteRepository.save(preferencia);

        return toResponse(actualizada);
    }

    @Transactional
    public void eliminar(UUID idPreferencia) {

        PreferenciaCliente preferencia = buscarEntidad(idPreferencia);

        preferenciaClienteRepository.delete(preferencia);
    }

    private PreferenciaCliente buscarEntidad(UUID idPreferencia) {

        return preferenciaClienteRepository.findById(idPreferencia)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Preferencia no encontrada: "
                                        + idPreferencia));
    }

    private PreferenciaClienteResponse toResponse(
            PreferenciaCliente preferencia) {

        return new PreferenciaClienteResponse(
                preferencia.getIdPreferencia(),
                preferencia.getPerfilCliente().getIdCuenta(),
                preferencia.getTipo(),
                preferencia.getDetalle()
        );
    }
}