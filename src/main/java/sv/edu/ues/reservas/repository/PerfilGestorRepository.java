package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.ues.reservas.domain.entity.PerfilGestor;

import java.util.UUID;

public interface PerfilGestorRepository
        extends JpaRepository<PerfilGestor, UUID> {
}