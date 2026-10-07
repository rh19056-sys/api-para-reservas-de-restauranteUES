package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sv.edu.ues.reservas.domain.enums.EstadoCuenta;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "cuenta", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_cuenta", nullable = false, updatable = false)
    private UUID idCuenta;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
            name = "estado_cuenta",
            nullable = false,
            columnDefinition = "reservas.estado_cuenta"
    )
    @Builder.Default
    private EstadoCuenta estadoCuenta = EstadoCuenta.PENDIENTE_VERIFICACION;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private OffsetDateTime fechaCreacion;

    @Column(name = "ultimo_acceso")
    private OffsetDateTime ultimoAcceso;

    @Column(name = "fecha_verificacion")
    private OffsetDateTime fechaVerificacion;

    @PrePersist
    protected void alCrear() {
        if (fechaCreacion == null) {
            fechaCreacion = OffsetDateTime.now();
        }
    }
}