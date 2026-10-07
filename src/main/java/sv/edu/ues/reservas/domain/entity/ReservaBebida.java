package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import sv.edu.ues.reservas.domain.entity.id.ReservaBebidaId;

// TODO: agregar @ManyToOne @MapsId hacia Reserva y Bebida
// cuando esas entidades existan en main.
@Entity
@Table(name = "reserva_bebida", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaBebida extends ConsumoReserva<ReservaBebidaId> {

    @EmbeddedId
    private ReservaBebidaId id;
}
