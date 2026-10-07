package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import sv.edu.ues.reservas.domain.entity.ConsumoReserva;
import sv.edu.ues.reservas.domain.entity.id.ConsumoReservaId;
import sv.edu.ues.reservas.dto.consumo.ConsumoActualizarRequest;
import sv.edu.ues.reservas.dto.consumo.ConsumoCrearRequest;
import sv.edu.ues.reservas.dto.consumo.ConsumoResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.ConsumoReservaRepository;

import java.util.List;
import java.util.UUID;

// Lógica común para agregar/listar/actualizar/quitar
// platos, menús y bebidas de una reserva.
public abstract class ConsumoReservaService<
        E extends ConsumoReserva<ID>,
        ID extends ConsumoReservaId
        > {

    protected abstract ConsumoReservaRepository<E, ID> repository();

    protected abstract ID crearId(UUID idReserva, UUID idProducto);

    protected abstract E crearEntidad(ID id);

    // Ej.: "El plato", "La bebida". Se usa en los mensajes de error.
    protected abstract String nombreProducto();

    @Transactional
    public ConsumoResponse agregar(
            UUID idReserva,
            ConsumoCrearRequest request
    ) {

        ID id = crearId(idReserva, request.idProducto());

        if (repository().existsById(id)) {
            throw new IllegalArgumentException(
                    nombreProducto() + " ya está en la reserva; actualice la cantidad"
            );
        }

        E consumo = crearEntidad(id);
        consumo.setCantidad(request.cantidad());
        consumo.setPrecioUnitario(request.precioUnitario());

        return toResponse(repository().save(consumo));
    }

    @Transactional
    public List<ConsumoResponse> obtenerPorReserva(UUID idReserva) {

        return repository().findByIdIdReserva(idReserva)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ConsumoResponse actualizar(
            UUID idReserva,
            UUID idProducto,
            ConsumoActualizarRequest request
    ) {

        E consumo = buscarEntidad(idReserva, idProducto);

        if (request.cantidad() != null) {
            consumo.setCantidad(request.cantidad());
        }

        if (request.precioUnitario() != null) {
            consumo.setPrecioUnitario(request.precioUnitario());
        }

        // saveAndFlush para que @PreUpdate recalcule el subtotal
        // antes de construir la respuesta.
        return toResponse(repository().saveAndFlush(consumo));
    }

    @Transactional
    public void eliminar(UUID idReserva, UUID idProducto) {

        repository().delete(buscarEntidad(idReserva, idProducto));
    }

    private E buscarEntidad(UUID idReserva, UUID idProducto) {

        return repository()
                .findById(crearId(idReserva, idProducto))
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                nombreProducto() + " " + idProducto
                                        + " no está en la reserva " + idReserva
                        )
                );
    }

    private ConsumoResponse toResponse(E consumo) {

        return new ConsumoResponse(
                consumo.getId().getIdReserva(),
                consumo.getId().idProducto(),
                consumo.getCantidad(),
                consumo.getPrecioUnitario(),
                consumo.getSubtotal()
        );
    }
}
