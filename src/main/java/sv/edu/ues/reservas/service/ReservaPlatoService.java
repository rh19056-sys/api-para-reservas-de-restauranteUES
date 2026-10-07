package sv.edu.ues.reservas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.ReservaPlato;
import sv.edu.ues.reservas.domain.entity.id.ReservaPlatoId;
import sv.edu.ues.reservas.repository.ConsumoReservaRepository;
import sv.edu.ues.reservas.repository.ReservaPlatoRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservaPlatoService
        extends ConsumoReservaService<ReservaPlato, ReservaPlatoId> {

    private final ReservaPlatoRepository reservaPlatoRepository;

    @Override
    protected ConsumoReservaRepository<ReservaPlato, ReservaPlatoId> repository() {
        return reservaPlatoRepository;
    }

    @Override
    protected ReservaPlatoId crearId(UUID idReserva, UUID idProducto) {
        return new ReservaPlatoId(idReserva, idProducto);
    }

    @Override
    protected ReservaPlato crearEntidad(ReservaPlatoId id) {
        return new ReservaPlato(id);
    }

    @Override
    protected String nombreProducto() {
        return "El plato";
    }
}
