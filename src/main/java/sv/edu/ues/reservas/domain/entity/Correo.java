package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sv.edu.ues.reservas.domain.enums.EstadoContacto;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "correo", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Correo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_correo", nullable = false, updatable = false)
    private UUID idCorreo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cuenta", nullable = false)
    private Cuenta cuenta;

    @Column(name = "correo", nullable = false, length = 255)
    private String direccionCorreo;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
            name = "estado_contacto",
            nullable = false,
            columnDefinition = "reservas.estado_contacto"
    )
    @Builder.Default
    private EstadoContacto estadoContacto = EstadoContacto.ACTIVO;

    @Column(name = "es_principal", nullable = false)
    @Builder.Default
    private Boolean esPrincipal = false;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private OffsetDateTime fechaCreacion;

    @PrePersist
    protected void alCrear() {
        if (fechaCreacion == null) {
            fechaCreacion = OffsetDateTime.now();
        }
    }
}