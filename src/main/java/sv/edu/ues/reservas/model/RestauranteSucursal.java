package sv.edu.ues.reservas.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "restaurante_sucursal")
public class RestauranteSucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(nullable = false)
    private String direccion;

    private String telefono;

    @NotNull(message = "La sucursal debe pertenecer a una organización")
    @ManyToOne
    @JoinColumn(name = "restaurante_org_id", nullable = false)
    private RestauranteOrg restauranteOrg;

    @OneToMany(mappedBy = "restauranteSucursal", cascade = CascadeType.ALL)
    private List<Mesa> mesas;

    @OneToMany(mappedBy = "restauranteSucursal", cascade = CascadeType.ALL)
    private List<HorarioRestaurante> horarios;

    @OneToMany(mappedBy = "restauranteSucursal", cascade = CascadeType.ALL)
    private List<PoliticaReserva> politicas;


    // CONSTRUCTORES

    public RestauranteSucursal() {
    }

    public RestauranteSucursal(String nombre, String direccion, String telefono, RestauranteOrg restauranteOrg) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.restauranteOrg = restauranteOrg;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public RestauranteOrg getRestauranteOrg() {
        return restauranteOrg;
    }

    public void setRestauranteOrg(RestauranteOrg restauranteOrg) {
        this.restauranteOrg = restauranteOrg;
    }

    public List<Mesa> getMesas() {
        return mesas;
    }

    public void setMesas(List<Mesa> mesas) {
        this.mesas = mesas;
    }

    public List<HorarioRestaurante> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioRestaurante> horarios) {
        this.horarios = horarios;
    }

    public List<PoliticaReserva> getPoliticas() {
        return politicas;
    }

    public void setPoliticas(List<PoliticaReserva> politicas) {
        this.politicas = politicas;
    }
}
//Validaciones (@NotBlank, @NotNull): Aseguramos que no se pueda crear una sucursal sin nombre, sin dirección o sin una organización matriz.
//cascade = CascadeType.ALL en las listas: Esto es un plus.
// Significa que si guardas o eliminas una sucursal,
// se guardarán o eliminarán en cascada sus mesas, horarios y políticas asociadas.
//
//
//
//
//
//
//