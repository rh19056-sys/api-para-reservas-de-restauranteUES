package sv.edu.ues.reservas.domain.entity.id;

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
public class ReservaMenuId implements Serializable {

    @Column(name = "id_reserva", nullable = false)
    private UUID idReserva;

    @Column(name = "id_menu", nullable = false)
    private UUID idMenu;
}
