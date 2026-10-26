package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Entidad que define las reglas de negocio (políticas) para las reservas
 * en una sucursal específica (ej. minutos de tolerancia, límite de personas).
 */
@Entity
@Table(name = "politica_reserva", schema = "reservas") // Mantenemos el nombre en singular , siguiendo ley de base de datos y evitar creacion de tabla fantasma
@Getter
@Setter
@NoArgsConstructor // Lombok genera el constructor vacío para JPA
public class PoliticaReserva {

    /**
     * Llave primaria de la política.
     * Usamos idPolitica para que coincida con el estándar de nombres de la tabla.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_politica", nullable = false, updatable = false)
    private UUID idPolitica;

    /**
     * Nombre descriptivo de la política (Ej: "Política VIP", "Política Estándar").
     */
    @NotBlank(message = "El nombre de la política es obligatorio")
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    /**
     * Descripción detallada de las reglas.
     */
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    /**
     * Tiempo que se le da al cliente de margen antes de cancelar su mesa.
     * Usamos Integer en lugar de int para permitir validaciones más estrictas y nulos controlados.
     */
    @Min(value = 0, message = "Los minutos de tolerancia no pueden ser negativos")
    @Column(name = "minutos_tolerancia", nullable = false)
    private Integer minutosTolerancia;

    /**
     * Cantidad máxima de personas permitidas por reserva bajo esta política.
     */
    @Min(value = 1, message = "El límite de personas debe ser al menos 1")
    @Column(name = "limite_personas", nullable = false)
    private Integer limitePersonasPorReserva;

    /**
     * Relación Muchos a Uno (N:1) con la Sucursal.
     * IMPORTANTE: El atributo se llama 'sucursal' para que coincida con el
     * 'mappedBy = "sucursal"' de la clase RestauranteSucursal.
     * FetchType.LAZY optimiza las consultas a la base de datos.
     */
    @NotNull(message = "La política debe pertenecer a una sucursal")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sucursal", nullable = false)
    private RestauranteSucursal sucursal;

}