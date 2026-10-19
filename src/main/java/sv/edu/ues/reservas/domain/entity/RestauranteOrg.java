package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.reservas.domain.enums.EstadoOrganizacion;

import java.util.UUID;

@Entity
@Table(name = "restaurante_org", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
public class RestauranteOrg {

    @Id
    @Column(name = "id_restaurante_org", nullable = false, updatable = false)
    private UUID idRestauranteOrg;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_organizacion", nullable = false)
    private EstadoOrganizacion estadoOrganizacion;
}