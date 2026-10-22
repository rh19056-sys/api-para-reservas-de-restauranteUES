package sv.edu.ues.reservas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.Menu;
import sv.edu.ues.reservas.domain.entity.Reserva;
import sv.edu.ues.reservas.domain.entity.ReservaMenu;
import sv.edu.ues.reservas.domain.entity.ReservaMenuId;
import sv.edu.ues.reservas.domain.enums.EstadoMenu;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.MenuRepository;
import sv.edu.ues.reservas.repository.ConsumoReservaRepository;
import sv.edu.ues.reservas.repository.ReservaMenuRepository;
import sv.edu.ues.reservas.validation.ReservaValidator;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservaMenuService
        extends ConsumoReservaService<ReservaMenu, ReservaMenuId, Menu> {

    private final ReservaMenuRepository reservaMenuRepository;
    private final MenuRepository menuRepository;
    private final ReservaValidator reservaValidator;

    @Override
    protected ConsumoReservaRepository<ReservaMenu, ReservaMenuId> repository() {
        return reservaMenuRepository;
    }

    @Override
    protected ReservaValidator reservaValidator() {
        return reservaValidator;
    }

    @Override
    protected ReservaMenuId crearId(UUID idReserva, UUID idProducto) {
        return new ReservaMenuId(idReserva, idProducto);
    }

    @Override
    protected ReservaMenu crearEntidad(Reserva reserva, Menu menu) {
        return new ReservaMenu(reserva, menu);
    }

    @Override
    protected Menu buscarProducto(UUID idMenu) {
        return menuRepository.findById(idMenu)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Menú no encontrado: " + idMenu
                        )
                );
    }

    @Override
    protected Menu productoDe(ReservaMenu consumo) {
        return consumo.getMenu();
    }

    @Override
    protected UUID sucursalDe(Menu menu) {
        return menu.getSucursal().getIdSucursal();
    }

    @Override
    protected boolean estaActivo(Menu menu) {
        return menu.getEstadoMenu() == EstadoMenu.ACTIVO;
    }

    @Override
    protected Optional<BigDecimal> precioCatalogo(Menu menu) {
        return Optional.ofNullable(menu.getPrecio());
    }

    @Override
    protected String nombreProducto() {
        return "El menú";
    }
}
