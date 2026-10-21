package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.Plato;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlatoRepository extends JpaRepository<Plato, UUID> {

    List<Plato> findBySucursal_IdSucursal(UUID idSucursal);
}
