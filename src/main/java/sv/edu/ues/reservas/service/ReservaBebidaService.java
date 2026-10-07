package sv.edu.ues.reservas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.ReservaBebida;
import sv.edu.ues.reservas.domain.entity.id.ReservaBebidaId;
import sv.edu.ues.reservas.repository.ConsumoReservaRepository;
import sv.edu.ues.reservas.repository.ReservaBebidaRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservaBebidaService
        extends ConsumoReservaService<ReservaBebida, ReservaBebidaId> {

    private final ReservaBebidaRepository reservaBebidaRepository;

    @Override
    protected ConsumoReservaRepository<ReservaBebida, ReservaBebidaId> repository() {
        return reservaBebidaRepository;
    }

    @Override
    protected ReservaBebidaId crearId(UUID idReserva, UUID idProducto) {
        return new ReservaBebidaId(idReserva, idProducto);
    }

    @Override
    protected ReservaBebida crearEntidad(ReservaBebidaId id) {
        return new ReservaBebida(id);
    }

    @Override
    protected String nombreProducto() {
        return "La bebida";
    }
}
