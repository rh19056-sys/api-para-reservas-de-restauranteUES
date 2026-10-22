package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReservaPlatoId implements Serializable, ConsumoReservaId {

    @Column(name = "id_reserva", nullable = false)
    private UUID idReserva;

    @Column(name = "id_plato", nullable = false)
    private UUID idPlato;

    @Override
    public UUID idProducto() {
        return idPlato;
    }
}
