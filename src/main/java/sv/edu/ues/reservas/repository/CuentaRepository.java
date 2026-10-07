package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.ues.reservas.domain.entity.Cuenta;

import java.util.UUID;

public interface CuentaRepository extends JpaRepository<Cuenta, UUID> {
}