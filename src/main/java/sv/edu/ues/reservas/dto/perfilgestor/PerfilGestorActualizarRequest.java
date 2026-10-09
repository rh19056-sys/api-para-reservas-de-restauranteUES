package sv.edu.ues.reservas.dto.perfilgestor;

import jakarta.validation.constraints.Size;

public record PerfilGestorActualizarRequest(

        @Size(
                max = 150,
                message = "El nombre no puede superar 150 caracteres"
        )
        String nombre

) {
}