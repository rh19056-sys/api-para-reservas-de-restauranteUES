package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.ues.reservas.domain.entity.PreferenciaCliente;

import java.util.List;
import java.util.UUID;

public interface PreferenciaClienteRepository
        extends JpaRepository<PreferenciaCliente, UUID> {

    List<PreferenciaCliente> findByPerfilCliente_IdCuenta(UUID idCuenta);
}