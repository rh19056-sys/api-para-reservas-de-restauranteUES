package sv.edu.ues.reservas.repository;

import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.ReservaBebida;
import sv.edu.ues.reservas.domain.entity.ReservaBebidaId;

@Repository
public interface ReservaBebidaRepository
        extends ConsumoReservaRepository<ReservaBebida, ReservaBebidaId> {
}
