package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import sv.edu.ues.reservas.domain.entity.id.ReservaMenuId;

import java.math.BigDecimal;

// TODO: agregar @ManyToOne @MapsId hacia Reserva y Menu
// cuando esas entidades existan en main.
@Entity
@Table(name = "reserva_menu", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaMenu {

    @EmbeddedId
    private ReservaMenuId id;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @PrePersist
    @PreUpdate
    protected void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }
}
