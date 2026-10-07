package sv.edu.ues.reservas.dto.perfilcliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PerfilClienteCrearRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(
                max = 150,
                message = "El nombre no puede superar 150 caracteres"
        )
        String nombre

) {
}