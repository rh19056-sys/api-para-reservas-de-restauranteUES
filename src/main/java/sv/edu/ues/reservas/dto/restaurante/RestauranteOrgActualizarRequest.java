package sv.edu.ues.reservas.dto.restaurante;

import jakarta.validation.constraints.Size;

public record RestauranteOrgActualizarRequest(

        @Size(
                min = 1,
                max = 200,
                message = "El nombre debe tener entre 1 y 200 caracteres"
        )
        String nombre

) {
}