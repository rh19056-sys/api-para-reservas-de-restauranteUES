package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.reservas.domain.enums.EstadoGestor;

import java.util.UUID;

@Entity
@Table(name = "gestor_restaurante", schema = "reservas")
@IdClass(GestorRestauranteId.class)
@Getter
@Setter
@NoArgsConstructor
public class GestorRestaurante {

    /**
     * Parte 1 de la llave primaria compuesta.
     */
    @Id
    @Column(name = "id_cuenta", nullable = false)
    private UUID idCuenta;

    /**
     * Parte 2 de la llave primaria compuesta.
     * El nombre de esta variable (idSucursal) DEBE ser idéntico al de GestorRestauranteId.
     */
    @Id
    @Column(name = "id_sucursal", nullable = false)
    private UUID idSucursal;

    /**
     * Relación Muchos a Uno para acceder a los datos de la sucursal.
     * Usamos insertable = false y updatable = false porque la columna "id_sucursal"
     * ya está siendo manejada por el campo @Id de arriba. Esto evita que Hibernate
     * intente insertar la misma columna dos veces y lance un error SQL.
     */
    @NotNull(message = "El gestor debe pertenecer a una sucursal")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sucursal", insertable = false, updatable = false)
    private RestauranteSucursal sucursal;

    @NotNull(message = "El estado del gestor es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_gestor", nullable = false)
    private EstadoGestor estadoGestor;

}