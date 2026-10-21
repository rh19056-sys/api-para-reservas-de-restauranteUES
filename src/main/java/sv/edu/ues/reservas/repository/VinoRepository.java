package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.Vino;

import java.util.List;
import java.util.UUID;

@Repository
public interface VinoRepository extends JpaRepository<Vino, UUID> {

    List<Vino> findByBebida_Sucursal_IdSucursal(UUID idSucursal);
}
