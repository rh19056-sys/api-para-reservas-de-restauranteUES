package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sv.edu.ues.reservas.domain.enums.FormatoComprobante;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "comprobante_pago", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComprobantePago {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_comprobante", nullable = false, updatable = false)
    private UUID idComprobante;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pago", nullable = false, unique = true)
    private Pago pago;

    @Column(
            name = "numero_comprobante",
            nullable = false,
            unique = true,
            length = 100
    )
    private String numeroComprobante;

    @Column(name = "fecha_emision", nullable = false)
    private OffsetDateTime fechaEmision;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
            name = "formato",
            nullable = false,
            columnDefinition = "reservas.formato_comprobante"
    )
    private FormatoComprobante formato;

    @Column(name = "referencia", length = 255)
    private String referencia;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "desglose", columnDefinition = "jsonb")
    private Map<String, Object> desglose;

    @PrePersist
    protected void alCrear() {
        if (fechaEmision == null) {
            fechaEmision = OffsetDateTime.now();
        }
    }
}
