package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "mesa", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_mesa", nullable = false, updatable = false)
    private UUID idMesa;

    // manteniendo la relacion que tenia el domain/entity
    @NotNull(message = "La mesa debe pertenecer a una sucursal")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sucursal", nullable = false)
    private RestauranteSucursal sucursal;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

// agregando los campos completos
    @Min(value = 1, message = "El número de mesa debe ser mayor a 0")
    @Column(name = "numero", nullable = false)
    private Integer numero;

    @NotBlank(message = "La ubicación es obligatoria")
    @Column(name = "ubicacion", nullable = false)
    private String ubicacion;



}
