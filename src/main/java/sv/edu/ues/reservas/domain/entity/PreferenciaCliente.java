package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "preferencia_cliente", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenciaCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
        name = "id_preferencia",
        nullable = false,
        updatable = false
    )
    private UUID idPreferencia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cuenta", nullable = false)
    private PerfilCliente perfilCliente;

    @Column(
        name = "tipo",
        nullable = false,
        length = 100
    )
    private String tipo;

    @Column(
        name = "detalle",
        nullable = false,
        columnDefinition = "TEXT"
    )
    private String detalle;
}