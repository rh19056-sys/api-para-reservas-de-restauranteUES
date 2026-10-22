package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reserva_menu", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
public class ReservaMenu extends ConsumoReserva<ReservaMenuId> {

    @EmbeddedId
    private ReservaMenuId id = new ReservaMenuId();

    @MapsId("idReserva")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    @MapsId("idMenu")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_menu", nullable = false)
    private Menu menu;

    public ReservaMenu(Reserva reserva, Menu menu) {
        this.id = new ReservaMenuId(reserva.getIdReserva(), menu.getIdMenu());
        this.reserva = reserva;
        this.menu = menu;
    }
}
