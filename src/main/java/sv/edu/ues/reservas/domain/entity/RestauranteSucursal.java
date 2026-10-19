package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.reservas.domain.enums.EstadoSucursal;

import java.util.UUID;

@Entity
@Table(name = "restaurante_sucursal", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
public class RestauranteSucursal {

    @Id
    @Column(name = "id_sucursal", nullable = false, updatable = false)
    private UUID idSucursal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_restaurante_org",
            nullable = false
    )
    private RestauranteOrg restauranteOrg;

    @Column(name = "direccion", nullable = false, length = 300)
    private String direccion;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "tipo_cocina", length = 100)
    private String tipoCocina;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_sucursal", nullable = false)
    private EstadoSucursal estadoSucursal;
}