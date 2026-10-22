package sv.edu.ues.reservas.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Table(name = "gestores_restaurante")

public class GestorRestaurante {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nombre;
    private String email;
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private RestauranteSucursal restauranteSucursal;

    public GestorRestaurante() {

    }
// getters y setters

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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelefono() {
        return telefono;
    }
public void setTelefono(String telefono) {
        this.telefono = telefono;
}
    public RestauranteSucursal getRestauranteSucursal() {
        return restauranteSucursal;
    }
    public void setRestauranteSucursal(RestauranteSucursal restauranteSucursal) {
        this.restauranteSucursal = restauranteSucursal;
    }

}
