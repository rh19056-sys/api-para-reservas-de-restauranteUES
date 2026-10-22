package sv.edu.ues.reservas.repository;

import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.ReservaPlato;
import sv.edu.ues.reservas.domain.entity.ReservaPlatoId;

@Repository
public interface ReservaPlatoRepository
        extends ConsumoReservaRepository<ReservaPlato, ReservaPlatoId> {
}
