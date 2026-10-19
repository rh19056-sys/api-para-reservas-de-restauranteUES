package sv.edu.ues.reservas.dto.restaurante;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RestauranteOrgCrearRequest(

        @NotBlank(message = "El nombre de la organización es obligatorio")
        @Size(
                max = 200,
                message = "El nombre de la organización no puede superar los 200 caracteres"
        )
        String nombre

) {
}