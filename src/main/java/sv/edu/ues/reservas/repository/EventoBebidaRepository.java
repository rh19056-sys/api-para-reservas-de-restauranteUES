package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.EventoBebida;
import sv.edu.ues.reservas.domain.entity.EventoBebidaId;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventoBebidaRepository extends JpaRepository<EventoBebida, EventoBebidaId> {

    List<EventoBebida> findByEvento_IdEvento(UUID idEvento);

    void deleteByEvento_IdEvento(UUID idEvento);
}
