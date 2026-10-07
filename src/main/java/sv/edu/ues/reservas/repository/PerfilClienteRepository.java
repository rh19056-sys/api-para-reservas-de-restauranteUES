package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.ues.reservas.domain.entity.PerfilCliente;

import java.util.UUID;

public interface PerfilClienteRepository
        extends JpaRepository<PerfilCliente, UUID> {
}