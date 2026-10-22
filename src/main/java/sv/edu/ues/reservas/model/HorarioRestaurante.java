package sv.edu.ues.reservas.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "horarios_restaurante")
public class HorarioRestaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "El día de la semana es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek diaSemana;

    @NotNull(message = "La hora de apertura es obligatoria")
    @Column(nullable = false)
    private LocalTime horaApertura;

    @NotNull(message = "La hora de cierre es obligatoria")
    @Column(nullable = false)
    private LocalTime horaCierre;

    @NotNull(message = "El horario debe pertenecer a una sucursal")
    @ManyToOne
    @JoinColumn(name = "sucursal_id", nullable = false)
    private RestauranteSucursal restauranteSucursal;

    // CONSTRUCTORES

    public HorarioRestaurante() {
    }

    public HorarioRestaurante(DayOfWeek diaSemana, LocalTime horaApertura, LocalTime horaCierre, RestauranteSucursal restauranteSucursal) {
        this.diaSemana = diaSemana;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
        this.restauranteSucursal = restauranteSucursal;
    }


    // GETTERS Y SETTERS

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(LocalTime horaApertura) {
        this.horaApertura = horaApertura;
    }

    public LocalTime getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(LocalTime horaCierre) {
        this.horaCierre = horaCierre;
    }

    public RestauranteSucursal getRestauranteSucursal() {
        return restauranteSucursal;
    }

    public void setRestauranteSucursal(RestauranteSucursal restauranteSucursal) {
        this.restauranteSucursal = restauranteSucursal;
    }
}