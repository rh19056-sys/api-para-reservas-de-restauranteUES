package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.ues.reservas.domain.entity.Telefono;

import java.util.List;
import java.util.UUID;

public interface TelefonoRepository extends JpaRepository<Telefono, UUID> {

    List<Telefono> findByCuenta_IdCuenta(UUID idCuenta);

    boolean existsByNumeroTelefono(String numeroTelefono);

    boolean existsByCuenta_IdCuentaAndEsPrincipalTrue(UUID idCuenta);

    boolean existsByCuenta_IdCuentaAndEsPrincipalTrueAndIdTelefonoNot(
            UUID idCuenta,
            UUID idTelefono
    );
}