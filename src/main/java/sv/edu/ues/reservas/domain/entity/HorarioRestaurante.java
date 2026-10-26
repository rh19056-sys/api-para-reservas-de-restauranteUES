package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Entidad que representa los horarios de apertura y cierre de una sucursal.
 * Mapea la tabla "horario_restaurante" dentro del esquema "reservas".
 */
@Entity
@Table(name = "horario_restaurante", schema = "reservas")
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
    private UUID idHorario;

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

    /**
     * Representa el día de la semana.
     * ¡Nota académica! En el DDL de la base de datos (04_tables.sql) este campo
     * está definido como SMALLINT (1=Lunes, 7=Domingo).
     * Por esta razón, usamos Integer en lugar de un Enum (DayOfWeek) para que
     * Hibernate haga "match" perfecto con la base de datos sin lanzar errores.
     */
    @NotNull(message = "El día de la semana es obligatorio")
    @Column(name = "dia_semana", nullable = false)
    private Integer diaSemana;

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
     * Fecha a partir de la cual aplica este horario (opcional).
     * Se usa LocalDate para fechas (YYYY-MM-DD) sin componente de tiempo.
     */
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    /**
     * Fecha hasta la cual aplica este horario (opcional).
     */
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

}