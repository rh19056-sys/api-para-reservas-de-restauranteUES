package sv.edu.ues.reservas.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "gestores_restaurante", schema = "reservas")
@Getter
@Setter
@NoArgsConstructor // Lombok se encarga de crear el constructor vacío
public class GestorRestaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_gestor", nullable = false, updatable = false)
    private UUID id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "telefono")
    private String telefono;

    // Relación con Sucursal (ya no hace falta poner la ruta completa sv.edu...)
    @NotNull(message = "El gestor debe pertenecer a una sucursal")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sucursal", nullable = false)
    private RestauranteSucursal sucursal;

}