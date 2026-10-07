package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.ues.reservas.domain.entity.ReservaMenu;
import sv.edu.ues.reservas.domain.entity.id.ReservaMenuId;

import java.util.List;
import java.util.UUID;

public interface ReservaMenuRepository
        extends JpaRepository<ReservaMenu, ReservaMenuId> {

    List<ReservaMenu> findByIdIdReserva(UUID idReserva);
}
