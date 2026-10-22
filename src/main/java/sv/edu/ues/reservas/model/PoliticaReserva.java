package sv.edu.ues.reservas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "politicas_reserva")
public class PoliticaReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "El nombre de la política es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Min(value = 0, message = "Los minutos de tolerancia no pueden ser negativos")
    @Column(nullable = false)
    private int minutosTolerancia;

    @Min(value = 1, message = "El límite de personas debe ser al menos 1")
    @Column(nullable = false)
    private int limitePersonasPorReserva;

    @NotNull(message = "La política debe pertenecer a una sucursal")
    @ManyToOne
    @JoinColumn(name = "sucursal_id", nullable = false)
    private RestauranteSucursal restauranteSucursal;


    // CONSTRUCTORES

    public PoliticaReserva() {
    }

    public PoliticaReserva(String nombre, String descripcion, int minutosTolerancia, int limitePersonasPorReserva, RestauranteSucursal restauranteSucursal) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.minutosTolerancia = minutosTolerancia;
        this.limitePersonasPorReserva = limitePersonasPorReserva;
        this.restauranteSucursal = restauranteSucursal;
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

    public int getMinutosTolerancia() {
        return minutosTolerancia;
    }

    public void setMinutosTolerancia(int minutosTolerancia) {
        this.minutosTolerancia = minutosTolerancia;
    }

    public int getLimitePersonasPorReserva() {
        return limitePersonasPorReserva;
    }

    public void setLimitePersonasPorReserva(int limitePersonasPorReserva) {
        this.limitePersonasPorReserva = limitePersonasPorReserva;
    }

    public RestauranteSucursal getRestauranteSucursal() {
        return restauranteSucursal;
    }

    public void setRestauranteSucursal(RestauranteSucursal restauranteSucursal) {
        this.restauranteSucursal = restauranteSucursal;
    }
}
//