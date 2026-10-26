package sv.edu.ues.reservas.domain.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Clase auxiliar requerida por JPA para manejar la Llave Primaria Compuesta
 * de la tabla "gestor_restaurante" (id_cuenta + id_sucursal).
 */
public class GestorRestauranteId implements Serializable {
    private UUID idCuenta;
    private UUID idSucursal;

    public GestorRestauranteId() {}

    public GestorRestauranteId(UUID idCuenta, UUID idSucursal) {
        this.idCuenta = idCuenta;
        this.idSucursal = idSucursal;
    }

    // equals y hashCode son OBLIGATORIOS para llaves compuestas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GestorRestauranteId that = (GestorRestauranteId) o;
        return Objects.equals(idCuenta, that.idCuenta) && Objects.equals(idSucursal, that.idSucursal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCuenta, idSucursal);
    }
}
