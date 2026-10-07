package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.ues.reservas.domain.entity.ReservaBebida;
import sv.edu.ues.reservas.domain.entity.id.ReservaBebidaId;

import java.util.List;
import java.util.UUID;

public interface ReservaBebidaRepository
        extends JpaRepository<ReservaBebida, ReservaBebidaId> {

    List<ReservaBebida> findByIdIdReserva(UUID idReserva);
}
