package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.ues.reservas.domain.entity.ComprobantePago;

import java.util.Optional;
import java.util.UUID;

public interface ComprobantePagoRepository
        extends JpaRepository<ComprobantePago, UUID> {

    Optional<ComprobantePago> findByPagoIdPago(UUID idPago);

    boolean existsByNumeroComprobante(String numeroComprobante);
}
