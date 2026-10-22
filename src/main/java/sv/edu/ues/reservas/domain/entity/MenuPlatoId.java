package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class MenuPlatoId implements Serializable {

    @Column(name = "id_menu")
    private UUID idMenu;

    @Column(name = "id_plato")
    private UUID idPlato;
}
