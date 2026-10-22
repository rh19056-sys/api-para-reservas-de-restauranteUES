package sv.edu.ues.reservas.dto.perfilgestor;

import java.util.UUID;

public record PerfilGestorResponse(
        UUID idCuenta,
        String nombre
) {
}