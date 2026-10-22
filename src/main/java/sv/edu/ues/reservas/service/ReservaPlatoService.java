package sv.edu.ues.reservas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.Plato;
import sv.edu.ues.reservas.domain.entity.Reserva;
import sv.edu.ues.reservas.domain.entity.ReservaPlato;
import sv.edu.ues.reservas.domain.entity.ReservaPlatoId;
import sv.edu.ues.reservas.domain.enums.EstadoProducto;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.PlatoRepository;
import sv.edu.ues.reservas.repository.ConsumoReservaRepository;
import sv.edu.ues.reservas.repository.ReservaPlatoRepository;
import sv.edu.ues.reservas.validation.ReservaValidator;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservaPlatoService
        extends ConsumoReservaService<ReservaPlato, ReservaPlatoId, Plato> {

    private final ReservaPlatoRepository reservaPlatoRepository;
    private final PlatoRepository platoRepository;
    private final ReservaValidator reservaValidator;

    @Override
    protected ConsumoReservaRepository<ReservaPlato, ReservaPlatoId> repository() {
        return reservaPlatoRepository;
    }

    @Override
    protected ReservaValidator reservaValidator() {
        return reservaValidator;
    }

    @Override
    protected ReservaPlatoId crearId(UUID idReserva, UUID idProducto) {
        return new ReservaPlatoId(idReserva, idProducto);
    }

    @Override
    protected ReservaPlato crearEntidad(Reserva reserva, Plato plato) {
        return new ReservaPlato(reserva, plato);
    }

    @Override
    protected Plato buscarProducto(UUID idPlato) {
        return platoRepository.findById(idPlato)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Plato no encontrado: " + idPlato
                        )
                );
    }

    @Override
    protected Plato productoDe(ReservaPlato consumo) {
        return consumo.getPlato();
    }

    @Override
    protected UUID sucursalDe(Plato plato) {
        return plato.getSucursal().getIdSucursal();
    }

    @Override
    protected boolean estaActivo(Plato plato) {
        return plato.getEstadoProducto() == EstadoProducto.ACTIVO;
    }

    @Override
    protected Optional<BigDecimal> precioCatalogo(Plato plato) {
        return Optional.ofNullable(plato.getPrecio());
    }

    @Override
    protected String nombreProducto() {
        return "El plato";
    }
}
