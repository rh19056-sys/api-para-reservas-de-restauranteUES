package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu_plato", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
public class MenuPlato {

    @EmbeddedId
    private MenuPlatoId id = new MenuPlatoId();

    @MapsId("idMenu")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_menu", nullable = false)
    private Menu menu;

    @MapsId("idPlato")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_plato", nullable = false)
    private Plato plato;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad = 1;

    @Column(name = "orden")
    private Integer orden;
}
