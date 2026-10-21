package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "vino", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
public class Vino {

    @Id
    @Column(name = "id_bebida", nullable = false, updatable = false)
    private UUID idBebida;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_bebida", nullable = false)
    private Bebida bebida;

    @Column(name = "marca", length = 150)
    private String marca;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @Column(name = "condiciones_conservacion", columnDefinition = "text")
    private String condicionesConservacion;

    @Column(name = "anada")
    private Integer anada;
}
