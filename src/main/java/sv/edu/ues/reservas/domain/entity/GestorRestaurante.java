package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.reservas.domain.enums.EstadoGestor;

import java.util.UUID;

/**
 * Entidad que representa la asignación de un Gestor a una Sucursal.
 * Mapea la tabla "gestor_restaurante" dentro del esquema "reservas".
 */
@Entity
@Table(name = "gestor_restaurante", schema = "reservas")
@IdClass(GestorRestauranteId.class) // <-- Le indica a JPA que use la llave compuesta
@Getter
@Setter
@NoArgsConstructor // Lombok se encarga de crear el constructor vacío
public class GestorRestaurante {

    /**
     * Llave primaria compuesta: Parte 1 (id_cuenta).
     * Relación lógica con la tabla perfil_gestor.
     * En la base de datos no hay nombre, email ni teléfono aquí;
     * esos datos viven en la tabla "cuenta" y "perfil_gestor". Esta tabla es
     * puramente relacional (Muchos a Muchos entre Gestor y Sucursal).
     */
    @Id
    @Column(name = "id_cuenta", nullable = false)
    private UUID idCuenta;

    /**
     * Llave primaria compuesta: Parte 2 (id_sucursal).
     * Representa la relación Muchos a Uno (N:1) con la entidad Sucursal.
     * - FetchType.LAZY: Optimiza el rendimiento. No carga los datos de la sucursal
     *   en memoria a menos que se solicite explícitamente.
     * - insertable = false, updatable = false: Evita que Hibernate intente insertar
     *   el id_sucursal dos veces (una por el campo @Id y otra por la relación @JoinColumn).
     */
    @Id
    @NotNull(message = "El gestor debe pertenecer a una sucursal")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sucursal", nullable = false, insertable = false, updatable = false)
    private RestauranteSucursal sucursal;

    /**
     * Estado del gestor en esta sucursal (ACTIVO, INACTIVO).
     * Se guarda como String (EnumType.STRING) para mantener la legibilidad en la BD.
     */
    @NotNull(message = "El estado del gestor es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_gestor", nullable = false)
    private EstadoGestor estadoGestor;

}