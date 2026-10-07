package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.ReservaMenu;
import sv.edu.ues.reservas.domain.entity.id.ReservaMenuId;
import sv.edu.ues.reservas.dto.consumo.ConsumoActualizarRequest;
import sv.edu.ues.reservas.dto.consumo.ConsumoCrearRequest;
import sv.edu.ues.reservas.dto.consumo.ConsumoResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.ReservaMenuRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservaMenuService {

    private final ReservaMenuRepository reservaMenuRepository;

    @Transactional
    public ConsumoResponse agregar(
            UUID idReserva,
            ConsumoCrearRequest request
    ) {

        ReservaMenuId id = new ReservaMenuId(idReserva, request.idProducto());

        if (reservaMenuRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "El menú ya está en la reserva; actualice la cantidad"
            );
        }

        ReservaMenu consumo = ReservaMenu.builder()
                .id(id)
                .cantidad(request.cantidad())
                .precioUnitario(request.precioUnitario())
                .build();

        return toResponse(reservaMenuRepository.save(consumo));
    }

    @Transactional
    public List<ConsumoResponse> obtenerPorReserva(UUID idReserva) {

        return reservaMenuRepository.findByIdIdReserva(idReserva)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ConsumoResponse actualizar(
            UUID idReserva,
            UUID idMenu,
            ConsumoActualizarRequest request
    ) {

        ReservaMenu consumo = buscarEntidad(idReserva, idMenu);

        if (request.cantidad() != null) {
            consumo.setCantidad(request.cantidad());
        }

        if (request.precioUnitario() != null) {
            consumo.setPrecioUnitario(request.precioUnitario());
        }

        return toResponse(reservaMenuRepository.saveAndFlush(consumo));
    }

    @Transactional
    public void eliminar(UUID idReserva, UUID idMenu) {

        reservaMenuRepository.delete(buscarEntidad(idReserva, idMenu));
    }

    private ReservaMenu buscarEntidad(UUID idReserva, UUID idMenu) {

        return reservaMenuRepository
                .findById(new ReservaMenuId(idReserva, idMenu))
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "El menú " + idMenu
                                        + " no está en la reserva " + idReserva
                        )
                );
    }

    private ConsumoResponse toResponse(ReservaMenu consumo) {

        return new ConsumoResponse(
                consumo.getId().getIdReserva(),
                consumo.getId().getIdMenu(),
                consumo.getCantidad(),
                consumo.getPrecioUnitario(),
                consumo.getSubtotal()
        );
    }
}
