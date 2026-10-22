package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.EventoMenu;
import sv.edu.ues.reservas.domain.entity.EventoMenuId;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventoMenuRepository extends JpaRepository<EventoMenu, EventoMenuId> {

    List<EventoMenu> findByEvento_IdEvento(UUID idEvento);

    void deleteByEvento_IdEvento(UUID idEvento);
}
