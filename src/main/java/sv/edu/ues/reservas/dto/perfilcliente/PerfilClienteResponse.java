package sv.edu.ues.reservas.dto.perfilcliente;

import java.util.UUID;

public record PerfilClienteResponse(

        UUID idCuenta,
        String nombre

) {
}

//No devolvemos Cuenta cuenta.