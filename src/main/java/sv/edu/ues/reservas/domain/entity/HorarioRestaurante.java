package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Entidad que representa los horarios de apertura y cierre de una sucursal.
 * Mapea la tabla "horarios_restaurante" dentro del esquema "reservas".
 */
@Entity
@Table(name = "horarios_restaurante", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor // Lombok genera el constructor vacío requerido por JPA
public class HorarioRestaurante {

    /**
     * Llave primaria (PK) de la tabla.
     * Se utiliza UUID para evitar colisiones de IDs en sistemas distribuidos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_horario", nullable = false, updatable = false)
    private UUID id;

    /**
     * Representa el día de la semana (Lunes, Martes, etc.).
     * @Enumerated(EnumType.STRING): Guarda el texto "MONDAY" en la BD, no un número.
     * Esto es clave para la mantenibilidad: si mañana se agrega un nuevo día,
     * los datos anteriores no se corrompen.
     */
    @NotNull(message = "El día de la semana es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DayOfWeek diaSemana;

    /**
     * Hora de apertura usando java.time.LocalTime.
     * Se usa LocalTime porque solo nos interesa la hora (HH:mm:ss), no la fecha.
     */
    @NotNull(message = "La hora de apertura es obligatoria")
    @Column(name = "hora_apertura", nullable = false)
    private LocalTime horaApertura;

    /**
     * Hora de cierre de la sucursal.
     */
    @NotNull(message = "La hora de cierre es obligatoria")
    @Column(name = "hora_cierre", nullable = false)
    private LocalTime horaCierre;

    /**
     * Relación de Muchos a Uno (N:1) con la entidad Sucursal.
     * - FetchType.LAZY: Optimiza el rendimiento. No carga los datos de la sucursal
     *   en memoria a menos que se solicite explícitamente (ej. sucursal.getNombre()).
     * - optional = false: La relación es obligatoria (no puede haber horario sin sucursal).
     */
    @NotNull(message = "El horario debe pertenecer a una sucursal")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sucursal", nullable = false)
    private RestauranteSucursal sucursal;

}