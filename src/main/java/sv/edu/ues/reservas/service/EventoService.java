package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.Bebida;
import sv.edu.ues.reservas.domain.entity.Evento;
import sv.edu.ues.reservas.domain.entity.EventoBebida;
import sv.edu.ues.reservas.domain.entity.EventoMenu;
import sv.edu.ues.reservas.domain.entity.EventoPlato;
import sv.edu.ues.reservas.domain.entity.Menu;
import sv.edu.ues.reservas.domain.entity.Plato;
import sv.edu.ues.reservas.domain.entity.RestauranteSucursal;
import sv.edu.ues.reservas.dto.evento.EventoActualizarRequest;
import sv.edu.ues.reservas.dto.evento.EventoCrearRequest;
import sv.edu.ues.reservas.dto.evento.EventoItemRequest;
import sv.edu.ues.reservas.dto.evento.EventoItemResponse;
import sv.edu.ues.reservas.dto.evento.EventoResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.BebidaRepository;
import sv.edu.ues.reservas.repository.EventoBebidaRepository;
import sv.edu.ues.reservas.repository.EventoMenuRepository;
import sv.edu.ues.reservas.repository.EventoPlatoRepository;
import sv.edu.ues.reservas.repository.EventoRepository;
import sv.edu.ues.reservas.repository.MenuRepository;
import sv.edu.ues.reservas.repository.PlatoRepository;
import sv.edu.ues.reservas.repository.RestauranteSucursalRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventoService {

    private static final Logger logger = LoggerFactory.getLogger(EventoService.class);

    private final EventoRepository eventoRepository;
    private final EventoMenuRepository eventoMenuRepository;
    private final EventoPlatoRepository eventoPlatoRepository;
    private final EventoBebidaRepository eventoBebidaRepository;
    private final RestauranteSucursalRepository sucursalRepository;
    private final MenuRepository menuRepository;
    private final PlatoRepository platoRepository;
    private final BebidaRepository bebidaRepository;

    @Transactional
    public EventoResponse crear(EventoCrearRequest request) {
        Evento evento = new Evento();
        evento.setSucursal(buscarSucursal(request.idSucursal()));
        evento.setTitulo(request.titulo().trim());
        evento.setTipo(request.tipo().trim());
        evento.setFechaEvento(request.fechaEvento());
        evento.setEstadoEvento(request.estadoEvento());
        evento = eventoRepository.save(evento);

        guardarMenus(evento, request.menus());
        guardarPlatos(evento, request.platos());
        guardarBebidas(evento, request.bebidas());
        logger.info("Evento creado. idEvento={}", evento.getIdEvento());
        return toResponse(evento);
    }

    @Transactional
    public EventoResponse actualizar(UUID id, EventoActualizarRequest request) {
        Evento evento = buscarEvento(id);
        if (request.idSucursal() != null) {
            RestauranteSucursal sucursal = buscarSucursal(request.idSucursal());
            validarElementosExistentesEnSucursal(evento, sucursal.getIdSucursal());
            evento.setSucursal(sucursal);
        }
        if (request.titulo() != null) {
            evento.setTitulo(request.titulo().trim());
        }
        if (request.tipo() != null) {
            evento.setTipo(request.tipo().trim());
        }
        if (request.fechaEvento() != null) {
            evento.setFechaEvento(request.fechaEvento());
        }
        if (request.estadoEvento() != null) {
            evento.setEstadoEvento(request.estadoEvento());
        }
        evento = eventoRepository.save(evento);

        if (request.menus() != null) {
            reemplazarMenus(evento, request.menus());
        }
        if (request.platos() != null) {
            reemplazarPlatos(evento, request.platos());
        }
        if (request.bebidas() != null) {
            reemplazarBebidas(evento, request.bebidas());
        }
        logger.info("Evento actualizado. idEvento={}", evento.getIdEvento());
        return toResponse(evento);
    }

    @Transactional
    public void eliminar(UUID id) {
        Evento evento = buscarEvento(id);
        eventoMenuRepository.deleteAll(eventoMenuRepository.findByEvento_IdEvento(id));
        eventoPlatoRepository.deleteAll(eventoPlatoRepository.findByEvento_IdEvento(id));
        eventoBebidaRepository.deleteAll(eventoBebidaRepository.findByEvento_IdEvento(id));
        eventoRepository.delete(evento);
        logger.info("Evento eliminado. idEvento={}", id);
    }

    @Transactional
    public EventoResponse obtenerPorId(UUID id) {
        return toResponse(buscarEvento(id));
    }

    @Transactional
    public List<EventoResponse> obtenerTodos() {
        return eventoRepository.findAll().stream().map(this::toResponse).toList();
    }

    private void guardarMenus(Evento evento, List<EventoItemRequest> items) {
        if (items == null) return;
        validarIdsUnicos(items);
        List<EventoMenu> relaciones = items.stream().map(item -> {
            Menu menu = menuRepository.findById(item.id())
                    .orElseThrow(() -> noEncontrado("Menú", item.id()));
            validarSucursal(evento, menu.getSucursal().getIdSucursal(), "menú");
            EventoMenu relacion = new EventoMenu();
            relacion.setEvento(evento);
            relacion.setMenu(menu);
            relacion.getId().setIdEvento(evento.getIdEvento());
            relacion.getId().setIdMenu(menu.getIdMenu());
            relacion.setCantidad(item.cantidad());
            relacion.setPrecioUnitario(item.precioUnitario());
            return relacion;
        }).toList();
        eventoMenuRepository.saveAll(relaciones);
    }

    private void guardarPlatos(Evento evento, List<EventoItemRequest> items) {
        if (items == null) return;
        validarIdsUnicos(items);
        List<EventoPlato> relaciones = items.stream().map(item -> {
            Plato plato = platoRepository.findById(item.id())
                    .orElseThrow(() -> noEncontrado("Plato", item.id()));
            validarSucursal(evento, plato.getSucursal().getIdSucursal(), "plato");
            EventoPlato relacion = new EventoPlato();
            relacion.setEvento(evento);
            relacion.setPlato(plato);
            relacion.getId().setIdEvento(evento.getIdEvento());
            relacion.getId().setIdPlato(plato.getIdPlato());
            relacion.setCantidad(item.cantidad());
            relacion.setPrecioUnitario(item.precioUnitario());
            return relacion;
        }).toList();
        eventoPlatoRepository.saveAll(relaciones);
    }

    private void guardarBebidas(Evento evento, List<EventoItemRequest> items) {
        if (items == null) return;
        validarIdsUnicos(items);
        List<EventoBebida> relaciones = items.stream().map(item -> {
            Bebida bebida = bebidaRepository.findById(item.id())
                    .orElseThrow(() -> noEncontrado("Bebida", item.id()));
            validarSucursal(evento, bebida.getSucursal().getIdSucursal(), "bebida");
            EventoBebida relacion = new EventoBebida();
            relacion.setEvento(evento);
            relacion.setBebida(bebida);
            relacion.getId().setIdEvento(evento.getIdEvento());
            relacion.getId().setIdBebida(bebida.getIdBebida());
            relacion.setCantidad(item.cantidad());
            relacion.setPrecioUnitario(item.precioUnitario());
            return relacion;
        }).toList();
        eventoBebidaRepository.saveAll(relaciones);
    }

    private void reemplazarMenus(Evento evento, List<EventoItemRequest> items) {
        eventoMenuRepository.deleteAll(eventoMenuRepository.findByEvento_IdEvento(evento.getIdEvento()));
        eventoMenuRepository.flush();
        guardarMenus(evento, items);
    }

    private void reemplazarPlatos(Evento evento, List<EventoItemRequest> items) {
        eventoPlatoRepository.deleteAll(eventoPlatoRepository.findByEvento_IdEvento(evento.getIdEvento()));
        eventoPlatoRepository.flush();
        guardarPlatos(evento, items);
    }

    private void reemplazarBebidas(Evento evento, List<EventoItemRequest> items) {
        eventoBebidaRepository.deleteAll(eventoBebidaRepository.findByEvento_IdEvento(evento.getIdEvento()));
        eventoBebidaRepository.flush();
        guardarBebidas(evento, items);
    }

    private void validarElementosExistentesEnSucursal(Evento evento, UUID idSucursal) {
        boolean menuDeOtraSucursal = eventoMenuRepository.findByEvento_IdEvento(evento.getIdEvento())
                .stream().anyMatch(item -> !item.getMenu().getSucursal().getIdSucursal().equals(idSucursal));
        boolean platoDeOtraSucursal = eventoPlatoRepository.findByEvento_IdEvento(evento.getIdEvento())
                .stream().anyMatch(item -> !item.getPlato().getSucursal().getIdSucursal().equals(idSucursal));
        boolean bebidaDeOtraSucursal = eventoBebidaRepository.findByEvento_IdEvento(evento.getIdEvento())
                .stream().anyMatch(item -> !item.getBebida().getSucursal().getIdSucursal().equals(idSucursal));
        if (menuDeOtraSucursal || platoDeOtraSucursal || bebidaDeOtraSucursal) {
            throw new IllegalArgumentException(
                    "No se puede cambiar la sucursal mientras el evento tenga productos de otra sucursal"
            );
        }
    }

    private void validarSucursal(Evento evento, UUID idSucursalElemento, String tipo) {
        if (!evento.getSucursal().getIdSucursal().equals(idSucursalElemento)) {
            throw new IllegalArgumentException(
                    "El " + tipo + " debe pertenecer a la misma sucursal que el evento"
            );
        }
    }

    private void validarIdsUnicos(List<EventoItemRequest> items) {
        if (items.stream().map(EventoItemRequest::id).distinct().count() != items.size()) {
            throw new IllegalArgumentException("No se permiten elementos duplicados en un evento");
        }
    }

    private Evento buscarEvento(UUID id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> noEncontrado("Evento", id));
    }

    private RestauranteSucursal buscarSucursal(UUID id) {
        return sucursalRepository.findById(id)
                .orElseThrow(() -> noEncontrado("Sucursal", id));
    }

    private RecursoNoEncontradoException noEncontrado(String tipo, UUID id) {
        return new RecursoNoEncontradoException(tipo + " no encontrado: " + id);
    }

    private EventoResponse toResponse(Evento evento) {
        return new EventoResponse(
                evento.getIdEvento(),
                evento.getSucursal().getIdSucursal(),
                evento.getTitulo(),
                evento.getTipo(),
                evento.getFechaEvento(),
                evento.getEstadoEvento(),
                eventoMenuRepository.findByEvento_IdEvento(evento.getIdEvento()).stream()
                        .map(item -> new EventoItemResponse(item.getMenu().getIdMenu(),
                                item.getCantidad(), item.getPrecioUnitario())).toList(),
                eventoPlatoRepository.findByEvento_IdEvento(evento.getIdEvento()).stream()
                        .map(item -> new EventoItemResponse(item.getPlato().getIdPlato(),
                                item.getCantidad(), item.getPrecioUnitario())).toList(),
                eventoBebidaRepository.findByEvento_IdEvento(evento.getIdEvento()).stream()
                        .map(item -> new EventoItemResponse(item.getBebida().getIdBebida(),
                                item.getCantidad(), item.getPrecioUnitario())).toList()
        );
    }
}
