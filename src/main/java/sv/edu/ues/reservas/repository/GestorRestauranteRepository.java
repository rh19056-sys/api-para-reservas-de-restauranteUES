package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.GestorRestaurante;
import sv.edu.ues.reservas.domain.entity.GestorRestauranteId;

import java.util.List;
import java.util.UUID;

@Repository
public interface GestorRestauranteRepository
        extends JpaRepository<GestorRestaurante, GestorRestauranteId> {

    List<GestorRestaurante> findByIdSucursal(UUID idSucursal);

    List<GestorRestaurante> findByIdCuenta(UUID idCuenta);

}