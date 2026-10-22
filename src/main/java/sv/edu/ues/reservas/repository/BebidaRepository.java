package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.Bebida;

import java.util.List;
import java.util.UUID;

@Repository
public interface BebidaRepository extends JpaRepository<Bebida, UUID> {

    List<Bebida> findBySucursal_IdSucursal(UUID idSucursal);
}
