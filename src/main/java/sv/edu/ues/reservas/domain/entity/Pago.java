package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sv.edu.ues.reservas.domain.enums.EstadoPago;
import sv.edu.ues.reservas.domain.enums.MetodoPago;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

// TODO: reemplazar idReserva por @ManyToOne Reserva
// cuando la entidad Reserva exista en main.
@Entity
@Table(name = "pago", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_pago", nullable = false, updatable = false)
    private UUID idPago;

    @Column(name = "id_reserva", nullable = false)
    private UUID idReserva;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
            name = "metodo_pago",
            nullable = false,
            columnDefinition = "reservas.metodo_pago"
    )
    private MetodoPago metodoPago;

    @Column(name = "monto", nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
            name = "estado_pago",
            nullable = false,
            columnDefinition = "reservas.estado_pago"
    )
    @Builder.Default
    private EstadoPago estadoPago = EstadoPago.PENDIENTE;

    @Column(name = "fecha_pago", nullable = false)
    private OffsetDateTime fechaPago;

    @PrePersist
    protected void alCrear() {
        if (fechaPago == null) {
            fechaPago = OffsetDateTime.now();
        }
    }
}
