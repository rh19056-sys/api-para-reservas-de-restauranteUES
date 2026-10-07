package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.ReservaBebida;
import sv.edu.ues.reservas.domain.entity.id.ReservaBebidaId;
import sv.edu.ues.reservas.dto.consumo.ConsumoActualizarRequest;
import sv.edu.ues.reservas.dto.consumo.ConsumoCrearRequest;
import sv.edu.ues.reservas.dto.consumo.ConsumoResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.ReservaBebidaRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservaBebidaService {

    private final ReservaBebidaRepository reservaBebidaRepository;

    @Transactional
    public ConsumoResponse agregar(
            UUID idReserva,
            ConsumoCrearRequest request
    ) {

        ReservaBebidaId id = new ReservaBebidaId(idReserva, request.idProducto());

        if (reservaBebidaRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "La bebida ya está en la reserva; actualice la cantidad"
            );
        }

        ReservaBebida consumo = ReservaBebida.builder()
                .id(id)
                .cantidad(request.cantidad())
                .precioUnitario(request.precioUnitario())
                .build();

        return toResponse(reservaBebidaRepository.save(consumo));
    }

    @Transactional
    public List<ConsumoResponse> obtenerPorReserva(UUID idReserva) {

        return reservaBebidaRepository.findByIdIdReserva(idReserva)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ConsumoResponse actualizar(
            UUID idReserva,
            UUID idBebida,
            ConsumoActualizarRequest request
    ) {

        ReservaBebida consumo = buscarEntidad(idReserva, idBebida);

        if (request.cantidad() != null) {
            consumo.setCantidad(request.cantidad());
        }

        if (request.precioUnitario() != null) {
            consumo.setPrecioUnitario(request.precioUnitario());
        }

        return toResponse(reservaBebidaRepository.saveAndFlush(consumo));
    }

    @Transactional
    public void eliminar(UUID idReserva, UUID idBebida) {

        reservaBebidaRepository.delete(buscarEntidad(idReserva, idBebida));
    }

    private ReservaBebida buscarEntidad(UUID idReserva, UUID idBebida) {

        return reservaBebidaRepository
                .findById(new ReservaBebidaId(idReserva, idBebida))
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "La bebida " + idBebida
                                        + " no está en la reserva " + idReserva
                        )
                );
    }

    private ConsumoResponse toResponse(ReservaBebida consumo) {

        return new ConsumoResponse(
                consumo.getId().getIdReserva(),
                consumo.getId().getIdBebida(),
                consumo.getCantidad(),
                consumo.getPrecioUnitario(),
                consumo.getSubtotal()
        );
    }
}
