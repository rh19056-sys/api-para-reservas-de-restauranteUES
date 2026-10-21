package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.MenuPlato;
import sv.edu.ues.reservas.domain.entity.MenuPlatoId;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuPlatoRepository extends JpaRepository<MenuPlato, MenuPlatoId> {

    List<MenuPlato> findByMenu_IdMenu(UUID idMenu);
}
