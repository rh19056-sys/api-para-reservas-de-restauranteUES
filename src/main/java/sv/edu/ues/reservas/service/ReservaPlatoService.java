package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.ReservaPlato;
import sv.edu.ues.reservas.domain.entity.id.ReservaPlatoId;
import sv.edu.ues.reservas.dto.consumo.ConsumoActualizarRequest;
import sv.edu.ues.reservas.dto.consumo.ConsumoCrearRequest;
import sv.edu.ues.reservas.dto.consumo.ConsumoResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.ReservaPlatoRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservaPlatoService {

    private final ReservaPlatoRepository reservaPlatoRepository;

    @Transactional
    public ConsumoResponse agregar(
            UUID idReserva,
            ConsumoCrearRequest request
    ) {

        ReservaPlatoId id = new ReservaPlatoId(idReserva, request.idProducto());

        if (reservaPlatoRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "El plato ya está en la reserva; actualice la cantidad"
            );
        }

        ReservaPlato consumo = ReservaPlato.builder()
                .id(id)
                .cantidad(request.cantidad())
                .precioUnitario(request.precioUnitario())
                .build();

        return toResponse(reservaPlatoRepository.save(consumo));
    }

    @Transactional
    public List<ConsumoResponse> obtenerPorReserva(UUID idReserva) {

        return reservaPlatoRepository.findByIdIdReserva(idReserva)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ConsumoResponse actualizar(
            UUID idReserva,
            UUID idPlato,
            ConsumoActualizarRequest request
    ) {

        ReservaPlato consumo = buscarEntidad(idReserva, idPlato);

        if (request.cantidad() != null) {
            consumo.setCantidad(request.cantidad());
        }

        if (request.precioUnitario() != null) {
            consumo.setPrecioUnitario(request.precioUnitario());
        }

        return toResponse(reservaPlatoRepository.saveAndFlush(consumo));
    }

    @Transactional
    public void eliminar(UUID idReserva, UUID idPlato) {

        reservaPlatoRepository.delete(buscarEntidad(idReserva, idPlato));
    }

    private ReservaPlato buscarEntidad(UUID idReserva, UUID idPlato) {

        return reservaPlatoRepository
                .findById(new ReservaPlatoId(idReserva, idPlato))
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "El plato " + idPlato
                                        + " no está en la reserva " + idReserva
                        )
                );
    }

    private ConsumoResponse toResponse(ReservaPlato consumo) {

        return new ConsumoResponse(
                consumo.getId().getIdReserva(),
                consumo.getId().getIdPlato(),
                consumo.getCantidad(),
                consumo.getPrecioUnitario(),
                consumo.getSubtotal()
        );
    }
}
