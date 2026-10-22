package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sv.edu.ues.reservas.domain.entity.ConsumoReserva;
import sv.edu.ues.reservas.domain.entity.ConsumoReservaId;
import sv.edu.ues.reservas.domain.entity.Reserva;
import sv.edu.ues.reservas.dto.consumo.ConsumoActualizarRequest;
import sv.edu.ues.reservas.dto.consumo.ConsumoCrearRequest;
import sv.edu.ues.reservas.dto.consumo.ConsumoResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.ConsumoReservaRepository;
import sv.edu.ues.reservas.validation.ReservaValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Lógica común para agregar/listar/actualizar/quitar
// platos, menús y bebidas de una reserva.
//
// E  = entidad de consumo (ReservaPlato, ...)
// ID = su PK compuesta
// P  = producto del catálogo (Plato, Menu, Bebida)
public abstract class ConsumoReservaService<
        E extends ConsumoReserva<ID>,
        ID extends ConsumoReservaId,
        P
        > {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract ConsumoReservaRepository<E, ID> repository();

    protected abstract ReservaValidator reservaValidator();

    protected abstract ID crearId(UUID idReserva, UUID idProducto);

    protected abstract E crearEntidad(Reserva reserva, P producto);

    protected abstract P buscarProducto(UUID idProducto);

    protected abstract P productoDe(E consumo);

    protected abstract UUID sucursalDe(P producto);

    protected abstract boolean estaActivo(P producto);

    // Vacío si el producto no tiene precio en el catálogo (bebida):
    // en ese caso el precio debe venir en la petición.
    protected abstract Optional<BigDecimal> precioCatalogo(P producto);

    // Ej.: "El plato", "La bebida". Se usa en los mensajes de error.
    protected abstract String nombreProducto();

    @Transactional
    public ConsumoResponse agregar(
            UUID idReserva,
            ConsumoCrearRequest request
    ) {

        Reserva reserva = reservaValidator().obtenerParaConsumo(idReserva);
        P producto = buscarProducto(request.idProducto());

        if (!estaActivo(producto)) {
            throw new IllegalArgumentException(
                    nombreProducto() + " no está activo"
            );
        }

        reservaValidator().validarMismaSucursal(
                reserva, sucursalDe(producto), nombreProducto()
        );

        if (repository().existsById(crearId(idReserva, request.idProducto()))) {
            throw new IllegalArgumentException(
                    nombreProducto() + " ya está en la reserva; actualice la cantidad"
            );
        }

        E consumo = crearEntidad(reserva, producto);
        consumo.setCantidad(request.cantidad());
        consumo.setPrecioUnitario(resolverPrecio(producto, request.precioUnitario()));

        E guardado = repository().save(consumo);
        logger.info(
                "Consumo agregado. idReserva={}, idProducto={}",
                idReserva, request.idProducto()
        );

        return toResponse(guardado);
    }

    @Transactional
    public List<ConsumoResponse> obtenerPorReserva(UUID idReserva) {

        reservaValidator().obtener(idReserva);

        return repository().findByReserva_IdReserva(idReserva)
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

        reservaValidator().obtenerParaConsumo(idReserva);
        E consumo = buscarEntidad(idReserva, idProducto);

        if (request.cantidad() != null) {
            consumo.setCantidad(request.cantidad());
        }

        if (request.precioUnitario() != null) {
            consumo.setPrecioUnitario(
                    resolverPrecio(productoDe(consumo), request.precioUnitario())
            );
        }

        // saveAndFlush para que @PreUpdate recalcule el subtotal
        // antes de construir la respuesta.
        E actualizado = repository().saveAndFlush(consumo);
        logger.info(
                "Consumo actualizado. idReserva={}, idProducto={}",
                idReserva, idProducto
        );

        return toResponse(actualizado);
    }

    @Transactional
    public void eliminar(UUID idReserva, UUID idProducto) {

        reservaValidator().obtenerParaConsumo(idReserva);
        repository().delete(buscarEntidad(idReserva, idProducto));
        logger.info(
                "Consumo eliminado. idReserva={}, idProducto={}",
                idReserva, idProducto
        );
    }

    // Plato y menú: precio del catálogo (no se acepta uno enviado).
    // Bebida: no tiene precio en BD, debe enviarse.
    private BigDecimal resolverPrecio(P producto, BigDecimal enviado) {

        Optional<BigDecimal> catalogo = precioCatalogo(producto);

        if (catalogo.isPresent()) {
            if (enviado != null) {
                throw new IllegalArgumentException(
                        nombreProducto()
                                + " usa el precio del catálogo; no envíe precioUnitario"
                );
            }
            return catalogo.get();
        }

        if (enviado == null) {
            throw new IllegalArgumentException(
                    "precioUnitario es obligatorio para "
                            + nombreProducto().toLowerCase()
            );
        }

        return enviado;
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
