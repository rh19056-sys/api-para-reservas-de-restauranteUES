package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.ReservaMesa;
import sv.edu.ues.reservas.domain.entity.ReservaMesaId;
import sv.edu.ues.reservas.domain.enums.EstadoReserva;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservaMesaRepository extends JpaRepository<ReservaMesa, ReservaMesaId> {

    List<ReservaMesa> findByReserva_IdReserva(UUID idReserva);

    void deleteByReserva_IdReserva(UUID idReserva);

    @Query("""
            select case when count(rm) > 0 then true else false end
            from ReservaMesa rm
            where rm.mesa.idMesa in :idMesas
              and rm.reserva.estadoReserva in :estados
              and rm.reserva.horaInicio < :horaFin
              and rm.reserva.horaFin > :horaInicio
              and (:idReserva is null or rm.reserva.idReserva <> :idReserva)
            """)
    boolean existeConflicto(
            @Param("idMesas") List<UUID> idMesas,
            @Param("horaInicio") OffsetDateTime horaInicio,
            @Param("horaFin") OffsetDateTime horaFin,
            @Param("idReserva") UUID idReserva,
            @Param("estados") List<EstadoReserva> estados
    );
}
