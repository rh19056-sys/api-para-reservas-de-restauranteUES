package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.RestauranteOrg;

import java.util.UUID;

@Repository
public interface RestauranteOrgRepository
        extends JpaRepository<RestauranteOrg, UUID> {
}