package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sv.edu.ues.reservas.domain.enums.EstadoReserva;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "reserva", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_reserva", nullable = false, updatable = false)
    private UUID idReserva;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cuenta_cliente", nullable = false)
    private PerfilCliente perfilCliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sucursal", nullable = false)
    private RestauranteSucursal sucursal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_politica")
    private PoliticaReserva politicaReserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento")
    private Evento evento;

    @Column(name = "numero_comensales", nullable = false)
    private Integer numeroComensales;

    @Column(name = "hora_inicio", nullable = false)
    private OffsetDateTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private OffsetDateTime horaFin;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "estado_reserva", nullable = false, columnDefinition = "reservas.estado_reserva")
    private EstadoReserva estadoReserva;

    @Column(name = "motivo_cancelacion")
    private String motivoCancelacion;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private OffsetDateTime fechaCreacion;

    @PrePersist
    protected void alCrear() {
        if (estadoReserva == null) {
            estadoReserva = EstadoReserva.PENDIENTE;
        }
        if (fechaCreacion == null) {
            fechaCreacion = OffsetDateTime.now();
        }
    }
}
