package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reserva_bebida", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
public class ReservaBebida extends ConsumoReserva<ReservaBebidaId> {

    @EmbeddedId
    private ReservaBebidaId id = new ReservaBebidaId();

    @MapsId("idReserva")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    @MapsId("idBebida")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_bebida", nullable = false)
    private Bebida bebida;

    public ReservaBebida(Reserva reserva, Bebida bebida) {
        this.id = new ReservaBebidaId(reserva.getIdReserva(), bebida.getIdBebida());
        this.reserva = reserva;
        this.bebida = bebida;
    }
}
