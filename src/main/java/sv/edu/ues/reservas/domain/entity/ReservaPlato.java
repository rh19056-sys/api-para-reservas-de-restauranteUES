package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import sv.edu.ues.reservas.domain.entity.id.ReservaPlatoId;

import java.math.BigDecimal;

// TODO: agregar @ManyToOne @MapsId hacia Reserva y Plato
// cuando esas entidades existan en main.
@Entity
@Table(name = "reserva_plato", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaPlato {

    @EmbeddedId
    private ReservaPlatoId id;

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
