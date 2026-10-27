package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sv.edu.ues.reservas.domain.enums.EstadoSucursal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad que representa una sucursal física del restaurante.
 * Almacena ubicación, capacidad y estado operativo.
 */
@Entity
@Table(name = "restaurante_sucursal", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor // Lombok genera el constructor vacío obligatorio para JPA
public class RestauranteSucursal {

    /**
     * Llave primaria de la sucursal.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_sucursal", nullable = false, updatable = false)
    private UUID idSucursal;

    /**
     * Relación Muchos a Uno (N:1) con la Organización principal.
     * Una organización puede tener muchas sucursales.
     */
    @NotNull(message = "La sucursal debe pertenecer a una organización")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_restaurante_org", nullable = false)
    private RestauranteOrg restauranteOrg;

    /**
     * Ubicación de la sucursal.
     */
    @NotBlank(message = "La dirección es obligatoria")
    @Column(name = "direccion", nullable = false, length = 300)
    private String direccion;

    /**
     * Datos operativos de la sucursal.
     */
    @NotNull(message = "La capacidad es obligatoria")
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "tipo_cocina", length = 100)
    private String tipoCocina;

    /**
     * Estado actual de la sucursal (ACTIVA, INACTIVA, MANTENIMIENTO).
     * En PostgreSQL es el tipo enum reservas.estado_sucursal, por eso se
     * mapea con NAMED_ENUM y columnDefinition.
     */
    @NotNull(message = "El estado de la sucursal es obligatorio")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "estado_sucursal", nullable = false, columnDefinition = "reservas.estado_sucursal")
    private EstadoSucursal estadoSucursal;


    // RELACIONES INVERSAS (OneToMany)
    // Nota: Usamos mappedBy = "sucursal" porque así se llama
    // el atributo en las clases Mesa, HorarioRestaurante y PoliticaReserva.
    // cascade = CascadeType.ALL permite que al guardar/eliminar una sucursal,
    // se afecten automáticamente sus mesas, horarios y políticas.

    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mesa> mesas = new ArrayList<>();

    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HorarioRestaurante> horarios = new ArrayList<>();

    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PoliticaReserva> politicas = new ArrayList<>();

}