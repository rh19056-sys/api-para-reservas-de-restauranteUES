package sv.edu.ues.reservas.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "restaurante_org")
public class RestauranteOrg {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "El nombre de la organización es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @OneToMany(mappedBy = "restauranteOrg", cascade = CascadeType.ALL)
    private List<RestauranteSucursal> sucursales;


    // CONSTRUCTORES

    public RestauranteOrg() {
    }

    public RestauranteOrg(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }


    // GETTERS Y SETTERS

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<RestauranteSucursal> getSucursales() {
        return sucursales;
    }

    public void setSucursales(List<RestauranteSucursal> sucursales) {
        this.sucursales = sucursales;
    }
}
