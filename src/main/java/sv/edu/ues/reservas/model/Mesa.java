package sv.edu.ues.reservas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "mesas")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Se elimina @NotNull porque 'int' por defecto es 0 y nunca es nulo.
    // El @Min(1) se encargará de validar que sea mayor a 0.
    @Min(value = 1, message = "El número de mesa debe ser mayor a 0")
    @Column(nullable = false)
    private int numero;

    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    @Column(nullable = false)
    private int capacidad;

    @NotBlank(message = "La ubicación es obligatoria")
    @Column(nullable = false)
    private String ubicacion;

    @NotNull(message = "La mesa debe pertenecer a una sucursal")
    @ManyToOne
    @JoinColumn(name = "sucursal_id", nullable = false)
    private RestauranteSucursal restauranteSucursal;

    // CONSTRUCTORES
    public Mesa() {
    }

    public Mesa(int numero, int capacidad, String ubicacion, RestauranteSucursal restauranteSucursal) {
        this.numero = numero;
        this.capacidad = capacidad;
        this.ubicacion = ubicacion;
        this.restauranteSucursal = restauranteSucursal;
    }

    // GETTERS Y SETTERS

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public RestauranteSucursal getRestauranteSucursal() {
        return restauranteSucursal;
    }

    public void setRestauranteSucursal(RestauranteSucursal restauranteSucursal) {
        this.restauranteSucursal = restauranteSucursal;
    }
}