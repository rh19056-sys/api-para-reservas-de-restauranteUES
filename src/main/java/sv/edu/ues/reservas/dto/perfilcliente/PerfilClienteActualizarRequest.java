package sv.edu.ues.reservas.dto.perfilcliente;

import jakarta.validation.constraints.Size;

public record PerfilClienteActualizarRequest(

        @Size(
                max = 150,
                message = "El nombre no puede superar 150 caracteres"
        )
        String nombre

) {
}

//--No ponemos @NotBlank aquí porque estamos haciendo una actualización 
//--parcial: si no se proporciona nombre, el Service conserva el existente.