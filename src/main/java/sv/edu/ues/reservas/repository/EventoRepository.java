package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.Evento;

import java.util.UUID;

@Repository
public interface EventoRepository extends JpaRepository<Evento, UUID> {
}
