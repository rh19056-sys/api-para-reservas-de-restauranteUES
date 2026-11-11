package sv.edu.ues.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.edu.ues.reservas.domain.entity.Mesa;

import java.util.List;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select mesa from Mesa mesa where mesa.idMesa = :id")
    Optional<Mesa> buscarPorIdConBloqueo(@Param("id") UUID id);
    // Buscar mesas pertenecientes a una sucursal
    List<Mesa> findBySucursal_IdSucursal(UUID idSucursal);

// Comprobar si ya existe un número de mesa en una sucursal
    boolean existsBySucursal_IdSucursalAndNumero(
        UUID idSucursal,
        Integer numero
    );

// Comprobar duplicados al actualizar, excluyendo la mesa actual
    boolean existsBySucursal_IdSucursalAndNumeroAndIdMesaNot(
        UUID idSucursal,
        Integer numero,
        UUID idMesa
    );
}
