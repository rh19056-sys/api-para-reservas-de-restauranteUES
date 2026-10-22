package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.ues.reservas.domain.entity.Correo;

import java.util.List;
import java.util.UUID;

public interface CorreoRepository extends JpaRepository<Correo, UUID> {

    List<Correo> findByCuenta_IdCuenta(UUID idCuenta);

    boolean existsByCuenta_IdCuenta(UUID idCuenta);

    boolean existsByCuenta_IdCuentaAndEsPrincipalTrue(UUID idCuenta);

    boolean existsByCuenta_IdCuentaAndEsPrincipalTrueAndIdCorreoNot(
            UUID idCuenta,
            UUID idCorreo
    );
}