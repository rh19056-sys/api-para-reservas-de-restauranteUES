package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "politica_reserva", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
public class PoliticaReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_politica", nullable = false, updatable = false)
    private UUID idPolitica;

    @NotNull(message = "El tipo de política es obligatorio")
    @Size(max = 100, message = "El tipo de política no puede exceder los 100 caracteres")
    @Column(name = "tipo", nullable = false, length = 100)
    private String tipo;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "casos_aplicacion", columnDefinition = "text")
    private String casosAplicacion;

    @NotNull(message = "La política debe pertenecer a una sucursal")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sucursal", nullable = false)
    private RestauranteSucursal sucursal;
}