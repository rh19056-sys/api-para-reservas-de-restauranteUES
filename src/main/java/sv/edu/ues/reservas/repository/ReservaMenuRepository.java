package sv.edu.ues.reservas.repository;

import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.ReservaMenu;
import sv.edu.ues.reservas.domain.entity.ReservaMenuId;

@Repository
public interface ReservaMenuRepository
        extends ConsumoReservaRepository<ReservaMenu, ReservaMenuId> {
}
