package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reserva_plato", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
public class ReservaPlato extends ConsumoReserva<ReservaPlatoId> {

    @EmbeddedId
    private ReservaPlatoId id = new ReservaPlatoId();

    @MapsId("idReserva")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    @MapsId("idPlato")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_plato", nullable = false)
    private Plato plato;

    public ReservaPlato(Reserva reserva, Plato plato) {
        this.id = new ReservaPlatoId(reserva.getIdReserva(), plato.getIdPlato());
        this.reserva = reserva;
        this.plato = plato;
    }
}
