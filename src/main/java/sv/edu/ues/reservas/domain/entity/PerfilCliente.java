package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "perfil_cliente",
        schema = "reservas"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilCliente {

    @Id
    @Column(
            name = "id_cuenta",
            nullable = false,
            updatable = false
    )
    private UUID idCuenta;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(
            name = "id_cuenta",
            nullable = false
    )
    private Cuenta cuenta;

    @Column(
            name = "nombre",
            nullable = false,
            length = 150
    )
    private String nombre;
}