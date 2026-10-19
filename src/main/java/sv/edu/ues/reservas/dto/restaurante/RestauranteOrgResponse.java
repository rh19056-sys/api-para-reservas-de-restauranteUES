package sv.edu.ues.reservas.dto.restaurante;

import sv.edu.ues.reservas.domain.enums.EstadoOrganizacion;

import java.util.UUID;

public record RestauranteOrgResponse(
        UUID idRestauranteOrg,
        String nombre,
        EstadoOrganizacion estadoOrganizacion
) {
}