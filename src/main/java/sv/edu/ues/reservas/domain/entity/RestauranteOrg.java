package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.reservas.domain.enums.EstadoOrganizacion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad principal que representa la Organización dueña del restaurante.
 * Es la raíz del agregado de sucursales.
 */
@Entity
@Table(name = "restaurante_org", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor // Lombok genera el constructor vacío para JPA
public class RestauranteOrg {

    /**
     * Llave primaria de la organización.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_restaurante_org", nullable = false, updatable = false)
    private UUID idRestauranteOrg;

    /**
     * Nombre comercial de la organización.
     * @NotBlank asegura que no se acepten cadenas vacías o con solo espacios.
     */
    @NotBlank(message = "El nombre de la organización es obligatorio")
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    /**
     * Estado actual de la organización (ACTIVA, INACTIVA, etc.).
     * Se guarda como texto (EnumType.STRING) para que la BD sea legible.
     */
    @NotNull(message = "El estado de la organización es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_organizacion", nullable = false)
    private EstadoOrganizacion estadoOrganizacion;

    /**
     * Relación Uno a Muchos (1:N) con las sucursales.
     * - mappedBy = "restauranteOrg": Hace referencia al atributo en la clase RestauranteSucursal.
     * - cascade = CascadeType.ALL: Si se guarda o elimina una organización, se propaga a sus sucursales.
     * - orphanRemoval = true: Si una sucursal se desvincula de la organización, se elimina de la BD.
     */
    @OneToMany(mappedBy = "restauranteOrg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestauranteSucursal> sucursales = new ArrayList<>();

}