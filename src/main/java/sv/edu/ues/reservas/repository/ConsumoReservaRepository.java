package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import sv.edu.ues.reservas.domain.entity.ConsumoReserva;
import sv.edu.ues.reservas.domain.entity.id.ConsumoReservaId;

import java.util.List;
import java.util.UUID;

@NoRepositoryBean
public interface ConsumoReservaRepository<
        E extends ConsumoReserva<ID>,
        ID extends ConsumoReservaId
        > extends JpaRepository<E, ID> {

    List<E> findByIdIdReserva(UUID idReserva);
}
