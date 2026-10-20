package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.Reserva;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, UUID> {

    List<Reserva> findByPerfilCliente_IdCuenta(UUID idCuenta);

    List<Reserva> findBySucursal_IdSucursal(UUID idSucursal);
}
