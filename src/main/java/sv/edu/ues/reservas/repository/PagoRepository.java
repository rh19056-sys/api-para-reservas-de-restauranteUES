package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.ues.reservas.domain.entity.Pago;

import java.util.List;
import java.util.UUID;

public interface PagoRepository extends JpaRepository<Pago, UUID> {

    List<Pago> findByIdReserva(UUID idReserva);
}
