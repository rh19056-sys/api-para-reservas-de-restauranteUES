package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.RestauranteSucursal;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestauranteSucursalRepository
        extends JpaRepository<RestauranteSucursal, UUID> {

    List<RestauranteSucursal> findByRestauranteOrg_idRestauranteOrg(
            UUID idRestauranteOrg
    );
}