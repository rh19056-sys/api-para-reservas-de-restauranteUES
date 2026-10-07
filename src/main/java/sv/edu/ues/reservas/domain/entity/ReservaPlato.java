package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import sv.edu.ues.reservas.domain.entity.id.ReservaPlatoId;

// TODO: agregar @ManyToOne @MapsId hacia Reserva y Plato
// cuando esas entidades existan en main.
@Entity
@Table(name = "reserva_plato", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaPlato extends ConsumoReserva<ReservaPlatoId> {

    @EmbeddedId
    private ReservaPlatoId id;
}
