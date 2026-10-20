package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sv.edu.ues.reservas.domain.enums.EstadoProducto;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "plato", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
public class Plato {

    @Id
    @Column(name = "id_plato", nullable = false, updatable = false)
    private UUID idPlato;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sucursal", nullable = false)
    private RestauranteSucursal sucursal;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @Column(name = "precio", nullable = false, precision = 12, scale = 2)
    private BigDecimal precio = BigDecimal.ZERO;

    @Column(name = "categoria", length = 100)
    private String categoria;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "estado_producto", nullable = false, columnDefinition = "reservas.estado_producto")
    private EstadoProducto estadoProducto = EstadoProducto.ACTIVO;
}
