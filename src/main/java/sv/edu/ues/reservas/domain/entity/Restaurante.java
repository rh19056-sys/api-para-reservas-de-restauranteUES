package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.reservas.domain.enums.EstadoRestaurante;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "restaurante", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_restaurante", nullable = false, updatable = false)
    private UUID idRestauranteOrg;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoRestaurante estado;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private OffsetDateTime fechaCreacion;

    @PrePersist
    protected void prePersist() {
        if (estado == null) {
            estado = EstadoRestaurante.ACTIVO;
        }

        if (fechaCreacion == null) {
            fechaCreacion = OffsetDateTime.now();
        }
    }
}