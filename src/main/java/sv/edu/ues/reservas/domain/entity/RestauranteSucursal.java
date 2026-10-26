package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.reservas.domain.enums.EstadoSucursal;

import java.util.List;
import java.util.UUID;

/**
 * Entidad que representa una sucursal física del restaurante.
 * Almacena información de contacto, capacidad y estado operativo.
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
     * Datos de contacto y ubicación de la sucursal.
     */
    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(name = "direccion", nullable = false, length = 300)
    private String direccion;

    @Column(name = "telefono", length = 20)
    private String telefono;

    /**
     * Datos operativos de la sucursal.
     */
    @NotNull(message = "La capacidad es obligatoria")
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "tipo_cocina", length = 100)
    private String tipoCocina;

    /**
     * Estado actual de la sucursal (Ej: ACTIVA, INACTIVA, EN_MANTENIMIENTO).
     * Se guarda como String para evitar corrupción de datos si el Enum cambia.
     */
    @NotNull(message = "El estado de la sucursal es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_sucursal", nullable = false)
    private EstadoSucursal estadoSucursal;


    // RELACIONES INVERSAS (OneToMany)
    // Nota: Usamos mappedBy = "sucursal" porque así se llama
    // el atributo en las clases Mesa, HorarioRestaurante y PoliticaReserva.
    // cascade = CascadeType.ALL permite que al guardar/eliminar una sucursal,
    // se afecten automáticamente sus mesas, horarios y políticas.

    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mesa> mesas;

    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HorarioRestaurante> horarios;

    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PoliticaReserva> politicas;

}