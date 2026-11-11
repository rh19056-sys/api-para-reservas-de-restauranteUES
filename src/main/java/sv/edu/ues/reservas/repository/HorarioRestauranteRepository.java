package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.HorarioRestaurante;

import java.util.List;
import java.util.UUID;

@Repository
public interface HorarioRestauranteRepository
        extends JpaRepository<HorarioRestaurante, UUID> {

    List<HorarioRestaurante> findBySucursal_IdSucursal(UUID idSucursal);

}