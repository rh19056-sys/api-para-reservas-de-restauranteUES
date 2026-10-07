package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import sv.edu.ues.reservas.domain.entity.id.ReservaMenuId;

// TODO: agregar @ManyToOne @MapsId hacia Reserva y Menu
// cuando esas entidades existan en main.
@Entity
@Table(name = "reserva_menu", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaMenu extends ConsumoReserva<ReservaMenuId> {

    @EmbeddedId
    private ReservaMenuId id;
}
