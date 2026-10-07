package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import sv.edu.ues.reservas.domain.entity.id.ConsumoReservaId;

import java.math.BigDecimal;

// Campos comunes de reserva_plato, reserva_menu y reserva_bebida.
@MappedSuperclass
@Getter
@Setter
public abstract class ConsumoReserva<ID extends ConsumoReservaId> {

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    public abstract ID getId();

    @PrePersist
    @PreUpdate
    protected void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }
}
