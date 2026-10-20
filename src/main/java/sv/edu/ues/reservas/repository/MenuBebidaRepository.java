package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.MenuBebida;
import sv.edu.ues.reservas.domain.entity.MenuBebidaId;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuBebidaRepository extends JpaRepository<MenuBebida, MenuBebidaId> {

    List<MenuBebida> findByMenu_IdMenu(UUID idMenu);
}
