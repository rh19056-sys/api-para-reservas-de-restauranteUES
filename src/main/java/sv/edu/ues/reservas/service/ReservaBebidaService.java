package sv.edu.ues.reservas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.Bebida;
import sv.edu.ues.reservas.domain.entity.Reserva;
import sv.edu.ues.reservas.domain.entity.ReservaBebida;
import sv.edu.ues.reservas.domain.entity.ReservaBebidaId;
import sv.edu.ues.reservas.domain.enums.EstadoProducto;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.BebidaRepository;
import sv.edu.ues.reservas.repository.ConsumoReservaRepository;
import sv.edu.ues.reservas.repository.ReservaBebidaRepository;
import sv.edu.ues.reservas.validation.ReservaValidator;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservaBebidaService
        extends ConsumoReservaService<ReservaBebida, ReservaBebidaId, Bebida> {

    private final ReservaBebidaRepository reservaBebidaRepository;
    private final BebidaRepository bebidaRepository;
    private final ReservaValidator reservaValidator;

    @Override
    protected ConsumoReservaRepository<ReservaBebida, ReservaBebidaId> repository() {
        return reservaBebidaRepository;
    }

    @Override
    protected ReservaValidator reservaValidator() {
        return reservaValidator;
    }

    @Override
    protected ReservaBebidaId crearId(UUID idReserva, UUID idProducto) {
        return new ReservaBebidaId(idReserva, idProducto);
    }

    @Override
    protected ReservaBebida crearEntidad(Reserva reserva, Bebida bebida) {
        return new ReservaBebida(reserva, bebida);
    }

    @Override
    protected Bebida buscarProducto(UUID idBebida) {
        return bebidaRepository.findById(idBebida)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Bebida no encontrada: " + idBebida
                        )
                );
    }

    @Override
    protected Bebida productoDe(ReservaBebida consumo) {
        return consumo.getBebida();
    }

    @Override
    protected UUID sucursalDe(Bebida bebida) {
        return bebida.getSucursal().getIdSucursal();
    }

    @Override
    protected boolean estaActivo(Bebida bebida) {
        return bebida.getEstadoBebida() == EstadoProducto.ACTIVO;
    }

    @Override
    protected Optional<BigDecimal> precioCatalogo(Bebida bebida) {
        return Optional.empty();
    }

    @Override
    protected String nombreProducto() {
        return "La bebida";
    }
}
