package sv.edu.ues.reservas.dto.cuenta;

import jakarta.validation.constraints.Size;

public record CuentaActualizarRequest(

        @Size(
                min = 8,
                max = 72,
                message = "La contraseña debe tener entre 8 y 72 caracteres"
        )
        String password,

        String estadoCuenta
) {
}