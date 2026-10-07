package sv.edu.ues.reservas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.ReservaMenu;
import sv.edu.ues.reservas.domain.entity.id.ReservaMenuId;
import sv.edu.ues.reservas.repository.ConsumoReservaRepository;
import sv.edu.ues.reservas.repository.ReservaMenuRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservaMenuService
        extends ConsumoReservaService<ReservaMenu, ReservaMenuId> {

    private final ReservaMenuRepository reservaMenuRepository;

    @Override
    protected ConsumoReservaRepository<ReservaMenu, ReservaMenuId> repository() {
        return reservaMenuRepository;
    }

    @Override
    protected ReservaMenuId crearId(UUID idReserva, UUID idProducto) {
        return new ReservaMenuId(idReserva, idProducto);
    }

    @Override
    protected ReservaMenu crearEntidad(ReservaMenuId id) {
        return new ReservaMenu(id);
    }

    @Override
    protected String nombreProducto() {
        return "El menú";
    }
}
