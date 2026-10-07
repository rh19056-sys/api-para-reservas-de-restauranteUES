package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.ues.reservas.domain.entity.ReservaPlato;
import sv.edu.ues.reservas.domain.entity.id.ReservaPlatoId;

import java.util.List;
import java.util.UUID;

public interface ReservaPlatoRepository
        extends JpaRepository<ReservaPlato, ReservaPlatoId> {

    List<ReservaPlato> findByIdIdReserva(UUID idReserva);
}
