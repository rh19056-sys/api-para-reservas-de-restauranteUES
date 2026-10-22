package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.Bebida;
import sv.edu.ues.reservas.domain.entity.Menu;
import sv.edu.ues.reservas.domain.entity.MenuBebida;
import sv.edu.ues.reservas.domain.entity.MenuBebidaId;
import sv.edu.ues.reservas.domain.entity.MenuPlato;
import sv.edu.ues.reservas.domain.entity.MenuPlatoId;
import sv.edu.ues.reservas.domain.entity.Plato;
import sv.edu.ues.reservas.domain.entity.RestauranteSucursal;
import sv.edu.ues.reservas.domain.entity.Vino;
import sv.edu.ues.reservas.domain.enums.EstadoMenu;
import sv.edu.ues.reservas.domain.enums.EstadoProducto;
import sv.edu.ues.reservas.dto.catalogo.BebidaActualizarRequest;
import sv.edu.ues.reservas.dto.catalogo.BebidaCrearRequest;
import sv.edu.ues.reservas.dto.catalogo.BebidaResponse;
import sv.edu.ues.reservas.dto.catalogo.MenuActualizarRequest;
import sv.edu.ues.reservas.dto.catalogo.MenuBebidaRequest;
import sv.edu.ues.reservas.dto.catalogo.MenuBebidaResponse;
import sv.edu.ues.reservas.dto.catalogo.MenuCrearRequest;
import sv.edu.ues.reservas.dto.catalogo.MenuPlatoRequest;
import sv.edu.ues.reservas.dto.catalogo.MenuPlatoResponse;
import sv.edu.ues.reservas.dto.catalogo.MenuResponse;
import sv.edu.ues.reservas.dto.catalogo.PlatoActualizarRequest;
import sv.edu.ues.reservas.dto.catalogo.PlatoCrearRequest;
import sv.edu.ues.reservas.dto.catalogo.PlatoResponse;
import sv.edu.ues.reservas.dto.catalogo.VinoActualizarRequest;
import sv.edu.ues.reservas.dto.catalogo.VinoCrearRequest;
import sv.edu.ues.reservas.dto.catalogo.VinoResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.BebidaRepository;
import sv.edu.ues.reservas.repository.MenuBebidaRepository;
import sv.edu.ues.reservas.repository.MenuPlatoRepository;
import sv.edu.ues.reservas.repository.MenuRepository;
import sv.edu.ues.reservas.repository.PlatoRepository;
import sv.edu.ues.reservas.repository.RestauranteSucursalRepository;
import sv.edu.ues.reservas.repository.VinoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatalogoService {

    private final MenuRepository menuRepository;
    private final PlatoRepository platoRepository;
    private final BebidaRepository bebidaRepository;
    private final VinoRepository vinoRepository;
    private final MenuPlatoRepository menuPlatoRepository;
    private final MenuBebidaRepository menuBebidaRepository;
    private final RestauranteSucursalRepository sucursalRepository;

    @Transactional
    public MenuResponse crearMenu(MenuCrearRequest request) {
        validarFechas(request.fechaInicio(), request.fechaFin());
        Menu menu = new Menu();
        menu.setIdMenu(UUID.randomUUID());
        menu.setSucursal(buscarSucursal(request.idSucursal()));
        menu.setNombre(request.nombre().trim());
        menu.setDescripcion(request.descripcion());
        menu.setTemporada(request.temporada());
        menu.setCantidadPersonas(request.cantidadPersonas());
        menu.setPrecio(request.precio());
        menu.setFechaInicio(request.fechaInicio());
        menu.setFechaFin(request.fechaFin());
        menu.setEstadoMenu(request.estadoMenu() == null ? EstadoMenu.ACTIVO : request.estadoMenu());
        return toResponse(menuRepository.save(menu));
    }

    @Transactional
    public MenuResponse actualizarMenu(UUID id, MenuActualizarRequest request) {
        Menu menu = buscarMenu(id);
        String nombre = request.nombre() == null ? menu.getNombre() : request.nombre().trim();
        LocalDate fechaInicio = request.fechaInicio() == null ? menu.getFechaInicio() : request.fechaInicio();
        LocalDate fechaFin = request.fechaFin() == null ? menu.getFechaFin() : request.fechaFin();
        validarNombre(nombre);
        validarFechas(fechaInicio, fechaFin);
        menu.setNombre(nombre);
        if (request.descripcion() != null) menu.setDescripcion(request.descripcion());
        if (request.temporada() != null) menu.setTemporada(request.temporada().trim());
        if (request.cantidadPersonas() != null) menu.setCantidadPersonas(request.cantidadPersonas());
        if (request.precio() != null) menu.setPrecio(request.precio());
        menu.setFechaInicio(fechaInicio);
        menu.setFechaFin(fechaFin);
        if (request.estadoMenu() != null) menu.setEstadoMenu(request.estadoMenu());
        return toResponse(menuRepository.save(menu));
    }

    @Transactional
    public void eliminarMenu(UUID id) {
        menuRepository.delete(buscarMenu(id));
    }

    @Transactional
    public MenuResponse obtenerMenu(UUID id) {
        return toResponse(buscarMenu(id));
    }

    @Transactional
    public List<MenuResponse> obtenerMenus(UUID idSucursal) {
        return (idSucursal == null ? menuRepository.findAll() : menusDeSucursal(idSucursal))
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public PlatoResponse crearPlato(PlatoCrearRequest request) {
        Plato plato = new Plato();
        plato.setIdPlato(UUID.randomUUID());
        plato.setSucursal(buscarSucursal(request.idSucursal()));
        plato.setNombre(request.nombre().trim());
        plato.setDescripcion(request.descripcion());
        plato.setPrecio(request.precio());
        plato.setCategoria(request.categoria());
        plato.setEstadoProducto(request.estadoProducto() == null
                ? EstadoProducto.ACTIVO : request.estadoProducto());
        return toResponse(platoRepository.save(plato));
    }

    @Transactional
    public PlatoResponse actualizarPlato(UUID id, PlatoActualizarRequest request) {
        Plato plato = buscarPlato(id);
        if (request.nombre() != null) {
            String nombre = request.nombre().trim();
            validarNombre(nombre);
            plato.setNombre(nombre);
        }
        if (request.descripcion() != null) plato.setDescripcion(request.descripcion());
        if (request.precio() != null) plato.setPrecio(request.precio());
        if (request.categoria() != null) plato.setCategoria(request.categoria().trim());
        if (request.estadoProducto() != null) plato.setEstadoProducto(request.estadoProducto());
        return toResponse(platoRepository.save(plato));
    }

    @Transactional
    public void eliminarPlato(UUID id) {
        platoRepository.delete(buscarPlato(id));
    }

    @Transactional
    public PlatoResponse obtenerPlato(UUID id) {
        return toResponse(buscarPlato(id));
    }

    @Transactional
    public List<PlatoResponse> obtenerPlatos(UUID idSucursal) {
        return (idSucursal == null ? platoRepository.findAll() : platosDeSucursal(idSucursal))
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public BebidaResponse crearBebida(BebidaCrearRequest request) {
        Bebida bebida = new Bebida();
        bebida.setIdBebida(UUID.randomUUID());
        bebida.setSucursal(buscarSucursal(request.idSucursal()));
        bebida.setNombre(request.nombre().trim());
        bebida.setDescripcion(request.descripcion());
        bebida.setEstadoBebida(request.estadoBebida() == null
                ? EstadoProducto.ACTIVO : request.estadoBebida());
        return toResponse(bebidaRepository.save(bebida));
    }

    @Transactional
    public BebidaResponse actualizarBebida(UUID id, BebidaActualizarRequest request) {
        Bebida bebida = buscarBebida(id);
        if (request.nombre() != null) {
            String nombre = request.nombre().trim();
            validarNombre(nombre);
            bebida.setNombre(nombre);
        }
        if (request.descripcion() != null) bebida.setDescripcion(request.descripcion());
        if (request.estadoBebida() != null) bebida.setEstadoBebida(request.estadoBebida());
        return toResponse(bebidaRepository.save(bebida));
    }

    @Transactional
    public void eliminarBebida(UUID id) {
        bebidaRepository.delete(buscarBebida(id));
    }

    @Transactional
    public BebidaResponse obtenerBebida(UUID id) {
        return toResponse(buscarBebida(id));
    }

    @Transactional
    public List<BebidaResponse> obtenerBebidas(UUID idSucursal) {
        return (idSucursal == null ? bebidaRepository.findAll() : bebidasDeSucursal(idSucursal))
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public VinoResponse crearVino(VinoCrearRequest request) {
        if (request.anada() != null && request.anada() > 2100) {
            throw new IllegalArgumentException("La añada debe estar entre 1800 y 2100");
        }
        Bebida bebida = new Bebida();
        bebida.setIdBebida(UUID.randomUUID());
        bebida.setSucursal(buscarSucursal(request.idSucursal()));
        bebida.setNombre(request.nombre().trim());
        bebida.setDescripcion(request.descripcion());
        bebida.setEstadoBebida(request.estadoBebida() == null
                ? EstadoProducto.ACTIVO : request.estadoBebida());
        bebida = bebidaRepository.save(bebida);

        Vino vino = new Vino();
        vino.setBebida(bebida);
        vino.setMarca(request.marca());
        vino.setStock(request.stock());
        vino.setCondicionesConservacion(request.condicionesConservacion());
        vino.setAnada(request.anada());
        return toResponse(vinoRepository.save(vino));
    }

    @Transactional
    public VinoResponse actualizarVino(UUID idBebida, VinoActualizarRequest request) {
        if (request.anada() != null && request.anada() > 2100) {
            throw new IllegalArgumentException("La añada debe estar entre 1800 y 2100");
        }
        Vino vino = buscarVino(idBebida);
        if (request.marca() != null) vino.setMarca(request.marca().trim());
        if (request.stock() != null) vino.setStock(request.stock());
        if (request.condicionesConservacion() != null) {
            vino.setCondicionesConservacion(request.condicionesConservacion());
        }
        if (request.anada() != null) vino.setAnada(request.anada());
        return toResponse(vinoRepository.save(vino));
    }

    @Transactional
    public void eliminarVino(UUID idBebida) {
        vinoRepository.delete(buscarVino(idBebida));
    }

    @Transactional
    public VinoResponse obtenerVino(UUID idBebida) {
        return toResponse(buscarVino(idBebida));
    }

    @Transactional
    public List<VinoResponse> obtenerVinos(UUID idSucursal) {
        if (idSucursal != null) validarSucursalExiste(idSucursal);
        List<Vino> vinos = idSucursal == null
                ? vinoRepository.findAll()
                : vinoRepository.findByBebida_Sucursal_IdSucursal(idSucursal);
        return vinos.stream()
                .map(this::toResponse).toList();
    }

    @Transactional
    public List<MenuPlatoResponse> obtenerPlatosMenu(UUID idMenu) {
        buscarMenu(idMenu);
        return menuPlatoRepository.findByMenu_IdMenu(idMenu).stream()
                .map(item -> new MenuPlatoResponse(item.getPlato().getIdPlato(),
                        item.getPlato().getNombre(), item.getCantidad(), item.getOrden()))
                .toList();
    }

    @Transactional
    public MenuPlatoResponse asignarPlato(UUID idMenu, MenuPlatoRequest request) {
        Menu menu = buscarMenu(idMenu);
        Plato plato = buscarPlato(request.idPlato());
        validarMismaSucursal(menu.getSucursal(), plato.getSucursal(), "plato");
        MenuPlatoId id = new MenuPlatoId();
        id.setIdMenu(idMenu);
        id.setIdPlato(plato.getIdPlato());
        if (menuPlatoRepository.existsById(id)) {
            throw new IllegalArgumentException("El plato ya está asociado al menú");
        }
        MenuPlato item = new MenuPlato();
        item.setId(id);
        item.setMenu(menu);
        item.setPlato(plato);
        item.setCantidad(request.cantidad());
        item.setOrden(request.orden());
        return toResponse(menuPlatoRepository.save(item));
    }

    @Transactional
    public MenuPlatoResponse actualizarPlatoMenu(UUID idMenu, UUID idPlato, MenuPlatoRequest request) {
        if (!idPlato.equals(request.idPlato())) {
            throw new IllegalArgumentException("El id del plato no coincide con la ruta");
        }
        MenuPlato item = menuPlatoRepository.findById(menuPlatoId(idMenu, idPlato))
                .orElseThrow(() -> noEncontrado("Relación menú-plato", idPlato));
        validarMismaSucursal(item.getMenu().getSucursal(), item.getPlato().getSucursal(), "plato");
        item.setCantidad(request.cantidad());
        item.setOrden(request.orden());
        return toResponse(menuPlatoRepository.save(item));
    }

    @Transactional
    public void quitarPlatoMenu(UUID idMenu, UUID idPlato) {
        menuPlatoRepository.delete(menuPlatoRepository.findById(menuPlatoId(idMenu, idPlato))
                .orElseThrow(() -> noEncontrado("Relación menú-plato", idPlato)));
    }

    @Transactional
    public List<MenuBebidaResponse> obtenerBebidasMenu(UUID idMenu) {
        buscarMenu(idMenu);
        return menuBebidaRepository.findByMenu_IdMenu(idMenu).stream()
                .map(item -> new MenuBebidaResponse(item.getBebida().getIdBebida(),
                        item.getBebida().getNombre(), item.getCantidad(), item.getOrden()))
                .toList();
    }

    @Transactional
    public MenuBebidaResponse asignarBebida(UUID idMenu, MenuBebidaRequest request) {
        Menu menu = buscarMenu(idMenu);
        Bebida bebida = buscarBebida(request.idBebida());
        validarMismaSucursal(menu.getSucursal(), bebida.getSucursal(), "bebida");
        MenuBebidaId id = new MenuBebidaId();
        id.setIdMenu(idMenu);
        id.setIdBebida(bebida.getIdBebida());
        if (menuBebidaRepository.existsById(id)) {
            throw new IllegalArgumentException("La bebida ya está asociada al menú");
        }
        MenuBebida item = new MenuBebida();
        item.setId(id);
        item.setMenu(menu);
        item.setBebida(bebida);
        item.setCantidad(request.cantidad());
        item.setOrden(request.orden());
        return toResponse(menuBebidaRepository.save(item));
    }

    @Transactional
    public MenuBebidaResponse actualizarBebidaMenu(UUID idMenu, UUID idBebida, MenuBebidaRequest request) {
        if (!idBebida.equals(request.idBebida())) {
            throw new IllegalArgumentException("El id de la bebida no coincide con la ruta");
        }
        MenuBebida item = menuBebidaRepository.findById(menuBebidaId(idMenu, idBebida))
                .orElseThrow(() -> noEncontrado("Relación menú-bebida", idBebida));
        validarMismaSucursal(item.getMenu().getSucursal(), item.getBebida().getSucursal(), "bebida");
        item.setCantidad(request.cantidad());
        item.setOrden(request.orden());
        return toResponse(menuBebidaRepository.save(item));
    }

    @Transactional
    public void quitarBebidaMenu(UUID idMenu, UUID idBebida) {
        menuBebidaRepository.delete(menuBebidaRepository.findById(menuBebidaId(idMenu, idBebida))
                .orElseThrow(() -> noEncontrado("Relación menú-bebida", idBebida)));
    }

    private List<Menu> menusDeSucursal(UUID idSucursal) {
        validarSucursalExiste(idSucursal);
        return menuRepository.findBySucursal_IdSucursal(idSucursal);
    }

    private List<Plato> platosDeSucursal(UUID idSucursal) {
        validarSucursalExiste(idSucursal);
        return platoRepository.findBySucursal_IdSucursal(idSucursal);
    }

    private List<Bebida> bebidasDeSucursal(UUID idSucursal) {
        validarSucursalExiste(idSucursal);
        return bebidaRepository.findBySucursal_IdSucursal(idSucursal);
    }

    private void validarSucursalExiste(UUID idSucursal) {
        if (!sucursalRepository.existsById(idSucursal)) {
            throw noEncontrado("Sucursal", idSucursal);
        }
    }

    private Menu buscarMenu(UUID id) {
        return menuRepository.findById(id).orElseThrow(() -> noEncontrado("Menú", id));
    }

    private Plato buscarPlato(UUID id) {
        return platoRepository.findById(id).orElseThrow(() -> noEncontrado("Plato", id));
    }

    private Bebida buscarBebida(UUID id) {
        return bebidaRepository.findById(id).orElseThrow(() -> noEncontrado("Bebida", id));
    }

    private Vino buscarVino(UUID id) {
        return vinoRepository.findById(id).orElseThrow(() -> noEncontrado("Vino", id));
    }

    private RestauranteSucursal buscarSucursal(UUID id) {
        return sucursalRepository.findById(id).orElseThrow(() -> noEncontrado("Sucursal", id));
    }

    private void validarMismaSucursal(RestauranteSucursal sucursalMenu,
                                      RestauranteSucursal sucursalProducto, String tipo) {
        if (!sucursalMenu.getIdSucursal().equals(sucursalProducto.getIdSucursal())) {
            throw new IllegalArgumentException(
                    "El " + tipo + " debe pertenecer a la misma sucursal que el menú");
        }
    }

    private void validarFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha inicio");
        }
    }

    private void validarNombre(String nombre) {
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
    }

    private MenuPlatoId menuPlatoId(UUID idMenu, UUID idPlato) {
        MenuPlatoId id = new MenuPlatoId();
        id.setIdMenu(idMenu);
        id.setIdPlato(idPlato);
        return id;
    }

    private MenuBebidaId menuBebidaId(UUID idMenu, UUID idBebida) {
        MenuBebidaId id = new MenuBebidaId();
        id.setIdMenu(idMenu);
        id.setIdBebida(idBebida);
        return id;
    }

    private RecursoNoEncontradoException noEncontrado(String tipo, UUID id) {
        return new RecursoNoEncontradoException(tipo + " no encontrado: " + id);
    }

    private MenuResponse toResponse(Menu menu) {
        return new MenuResponse(menu.getIdMenu(), menu.getSucursal().getIdSucursal(),
                menu.getNombre(), menu.getDescripcion(), menu.getTemporada(),
                menu.getCantidadPersonas(), menu.getPrecio(), menu.getFechaInicio(),
                menu.getFechaFin(), menu.getEstadoMenu());
    }

    private PlatoResponse toResponse(Plato plato) {
        return new PlatoResponse(plato.getIdPlato(), plato.getSucursal().getIdSucursal(),
                plato.getNombre(), plato.getDescripcion(), plato.getPrecio(),
                plato.getCategoria(), plato.getEstadoProducto());
    }

    private BebidaResponse toResponse(Bebida bebida) {
        return new BebidaResponse(bebida.getIdBebida(), bebida.getSucursal().getIdSucursal(),
                bebida.getNombre(), bebida.getDescripcion(), bebida.getEstadoBebida(),
                vinoRepository.existsById(bebida.getIdBebida()));
    }

    private VinoResponse toResponse(Vino vino) {
        Bebida bebida = vino.getBebida();
        return new VinoResponse(vino.getIdBebida(), bebida.getSucursal().getIdSucursal(),
                bebida.getNombre(), bebida.getDescripcion(), vino.getMarca(),
                vino.getStock(), vino.getCondicionesConservacion(), vino.getAnada());
    }

    private MenuPlatoResponse toResponse(MenuPlato item) {
        return new MenuPlatoResponse(item.getPlato().getIdPlato(),
                item.getPlato().getNombre(), item.getCantidad(), item.getOrden());
    }

    private MenuBebidaResponse toResponse(MenuBebida item) {
        return new MenuBebidaResponse(item.getBebida().getIdBebida(),
                item.getBebida().getNombre(), item.getCantidad(), item.getOrden());
    }
}
