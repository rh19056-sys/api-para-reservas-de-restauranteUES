package sv.edu.ues.reservas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sv.edu.ues.reservas.domain.entity.Evento;
import sv.edu.ues.reservas.domain.entity.Mesa;
import sv.edu.ues.reservas.domain.entity.PerfilCliente;
import sv.edu.ues.reservas.domain.entity.PoliticaReserva;
import sv.edu.ues.reservas.domain.entity.Reserva;
import sv.edu.ues.reservas.domain.entity.ReservaMesa;
import sv.edu.ues.reservas.domain.entity.RestauranteSucursal;
import sv.edu.ues.reservas.domain.enums.EstadoReserva;
import sv.edu.ues.reservas.dto.reserva.ReservaActualizarRequest;
import sv.edu.ues.reservas.dto.reserva.ReservaCrearRequest;
import sv.edu.ues.reservas.dto.reserva.ReservaResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.EventoRepository;
import sv.edu.ues.reservas.repository.MesaRepository;
import sv.edu.ues.reservas.repository.PerfilClienteRepository;
import sv.edu.ues.reservas.repository.PoliticaReservaRepository;
import sv.edu.ues.reservas.repository.ReservaMesaRepository;
import sv.edu.ues.reservas.repository.ReservaRepository;
import sv.edu.ues.reservas.repository.RestauranteSucursalRepository;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);
    private static final List<EstadoReserva> ESTADOS_QUE_RESERVAN_MESA =
            List.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA);

    private final ReservaRepository reservaRepository;
    private final ReservaMesaRepository reservaMesaRepository;
    private final PerfilClienteRepository perfilClienteRepository;
    private final RestauranteSucursalRepository sucursalRepository;
    private final PoliticaReservaRepository politicaRepository;
    private final EventoRepository eventoRepository;
    private final MesaRepository mesaRepository;

    @Transactional
    public ReservaResponse crear(ReservaCrearRequest request) {
        Reserva reserva = new Reserva();
        reserva.setPerfilCliente(buscarCliente(request.idCuentaCliente()));
        reserva.setSucursal(buscarSucursal(request.idSucursal()));
        reserva.setPoliticaReserva(request.idPolitica() == null
                ? null : buscarPolitica(request.idPolitica()));
        reserva.setEvento(request.idEvento() == null ? null : buscarEvento(request.idEvento()));
        reserva.setNumeroComensales(request.numeroComensales());
        reserva.setHoraInicio(request.horaInicio());
        reserva.setHoraFin(request.horaFin());
        reserva.setEstadoReserva(request.estadoReserva() == null
                ? EstadoReserva.PENDIENTE : request.estadoReserva());
        reserva.setMotivoCancelacion(request.motivoCancelacion());
        validarReserva(reserva, request.idsMesas(), null);
        reserva = reservaRepository.save(reserva);
        guardarMesas(reserva, request.idsMesas());
        logger.info("Reserva creada. idReserva={}", reserva.getIdReserva());
        return toResponse(reserva);
    }

    @Transactional
    public ReservaResponse actualizar(UUID id, ReservaActualizarRequest request) {
        Reserva reserva = buscarReserva(id);
        if (request.idCuentaCliente() != null) {
            reserva.setPerfilCliente(buscarCliente(request.idCuentaCliente()));
        }
        if (request.idSucursal() != null) {
            reserva.setSucursal(buscarSucursal(request.idSucursal()));
        }
        if (request.idPolitica() != null) {
            reserva.setPoliticaReserva(buscarPolitica(request.idPolitica()));
        }
        if (request.idEvento() != null) {
            reserva.setEvento(buscarEvento(request.idEvento()));
        }
        if (request.numeroComensales() != null) {
            reserva.setNumeroComensales(request.numeroComensales());
        }
        if (request.horaInicio() != null) {
            reserva.setHoraInicio(request.horaInicio());
        }
        if (request.horaFin() != null) {
            reserva.setHoraFin(request.horaFin());
        }
        if (request.estadoReserva() != null) {
            reserva.setEstadoReserva(request.estadoReserva());
        }
        if (request.motivoCancelacion() != null) {
            reserva.setMotivoCancelacion(request.motivoCancelacion());
        }
        List<UUID> idsMesas = request.idsMesas() == null
                ? reservaMesaRepository.findByReserva_IdReserva(id).stream()
                        .map(item -> item.getMesa().getIdMesa()).toList()
                : request.idsMesas();
        validarReserva(reserva, idsMesas, id);
        reserva = reservaRepository.save(reserva);
        if (request.idsMesas() != null) {
            reemplazarMesas(reserva, idsMesas);
        }
        logger.info("Reserva actualizada. idReserva={}", reserva.getIdReserva());
        return toResponse(reserva);
    }

    @Transactional
    public void eliminar(UUID id) {
        Reserva reserva = buscarReserva(id);
        reservaMesaRepository.deleteAll(reservaMesaRepository.findByReserva_IdReserva(id));
        reservaRepository.delete(reserva);
        logger.info("Reserva eliminada. idReserva={}", id);
    }

    @Transactional
    public ReservaResponse obtenerPorId(UUID id) {
        return toResponse(buscarReserva(id));
    }

    @Transactional
    public List<ReservaResponse> obtenerTodas() {
        return reservaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public List<ReservaResponse> obtenerPorCliente(UUID idCuentaCliente) {
        if (!perfilClienteRepository.existsById(idCuentaCliente)) {
            throw noEncontrado("Perfil de cliente", idCuentaCliente);
        }
        return reservaRepository.findByPerfilCliente_IdCuenta(idCuentaCliente)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public List<ReservaResponse> obtenerPorSucursal(UUID idSucursal) {
        if (!sucursalRepository.existsById(idSucursal)) {
            throw noEncontrado("Sucursal", idSucursal);
        }
        return reservaRepository.findBySucursal_IdSucursal(idSucursal)
                .stream().map(this::toResponse).toList();
    }

    private void validarReserva(Reserva reserva, List<UUID> idsMesas, UUID idReserva) {
        if (reserva.getHoraInicio() == null || reserva.getHoraFin() == null
                || !reserva.getHoraFin().isAfter(reserva.getHoraInicio())) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }
        if (reserva.getNumeroComensales() == null || reserva.getNumeroComensales() < 1) {
            throw new IllegalArgumentException("El número de comensales debe ser mayor que cero");
        }
        if (idsMesas == null || idsMesas.isEmpty()) {
            throw new IllegalArgumentException("La reserva debe tener al menos una mesa");
        }
        if (new HashSet<>(idsMesas).size() != idsMesas.size()) {
            throw new IllegalArgumentException("No se permiten mesas duplicadas en una reserva");
        }
        UUID idSucursal = reserva.getSucursal().getIdSucursal();
        if (reserva.getPoliticaReserva() != null
                && !reserva.getPoliticaReserva().getSucursal().getIdSucursal().equals(idSucursal)) {
            throw new IllegalArgumentException("La política de reserva debe pertenecer a la misma sucursal");
        }
        if (reserva.getEvento() != null
                && !reserva.getEvento().getSucursal().getIdSucursal().equals(idSucursal)) {
            throw new IllegalArgumentException("El evento debe pertenecer a la misma sucursal");
        }

        List<Mesa> mesas = idsMesas.stream().sorted().map(this::buscarMesa).toList();
        if (mesas.stream().anyMatch(mesa ->
                !mesa.getSucursal().getIdSucursal().equals(idSucursal))) {
            throw new IllegalArgumentException("Todas las mesas deben pertenecer a la sucursal de la reserva");
        }
        int capacidad = mesas.stream().mapToInt(Mesa::getCapacidad).sum();
        if (capacidad < reserva.getNumeroComensales()) {
            throw new IllegalArgumentException(
                    "La capacidad total de las mesas no cubre el número de comensales"
            );
        }
        if (ESTADOS_QUE_RESERVAN_MESA.contains(reserva.getEstadoReserva())
                && reservaMesaRepository.existeConflicto(
                        idsMesas,
                        reserva.getHoraInicio(),
                        reserva.getHoraFin(),
                        idReserva,
                        ESTADOS_QUE_RESERVAN_MESA
                )) {
            throw new IllegalArgumentException("Una o más mesas ya están reservadas en ese horario");
        }
    }

    private void guardarMesas(Reserva reserva, List<UUID> idsMesas) {
        List<ReservaMesa> relaciones = idsMesas.stream().map(idMesa -> {
            Mesa mesa = buscarMesa(idMesa);
            ReservaMesa relacion = new ReservaMesa();
            relacion.setReserva(reserva);
            relacion.setMesa(mesa);
            relacion.getId().setIdReserva(reserva.getIdReserva());
            relacion.getId().setIdMesa(mesa.getIdMesa());
            return relacion;
        }).toList();
        reservaMesaRepository.saveAll(relaciones);
    }

    private void reemplazarMesas(Reserva reserva, List<UUID> idsMesas) {
        reservaMesaRepository.deleteAll(
                reservaMesaRepository.findByReserva_IdReserva(reserva.getIdReserva())
        );
        reservaMesaRepository.flush();
        guardarMesas(reserva, idsMesas);
    }

    private Reserva buscarReserva(UUID id) {
        return reservaRepository.findById(id).orElseThrow(() -> noEncontrado("Reserva", id));
    }

    private PerfilCliente buscarCliente(UUID id) {
        return perfilClienteRepository.findById(id)
                .orElseThrow(() -> noEncontrado("Perfil de cliente", id));
    }

    private RestauranteSucursal buscarSucursal(UUID id) {
        return sucursalRepository.findById(id).orElseThrow(() -> noEncontrado("Sucursal", id));
    }

    private PoliticaReserva buscarPolitica(UUID id) {
        return politicaRepository.findById(id)
                .orElseThrow(() -> noEncontrado("Política de reserva", id));
    }

    private Evento buscarEvento(UUID id) {
        return eventoRepository.findById(id).orElseThrow(() -> noEncontrado("Evento", id));
    }

    private Mesa buscarMesa(UUID id) {
        return mesaRepository.buscarPorIdConBloqueo(id)
                .orElseThrow(() -> noEncontrado("Mesa", id));
    }

    private RecursoNoEncontradoException noEncontrado(String tipo, UUID id) {
        return new RecursoNoEncontradoException(tipo + " no encontrado: " + id);
    }

    private ReservaResponse toResponse(Reserva reserva) {
        return new ReservaResponse(
                reserva.getIdReserva(),
                reserva.getPerfilCliente().getIdCuenta(),
                reserva.getSucursal().getIdSucursal(),
                reserva.getPoliticaReserva() == null ? null
                        : reserva.getPoliticaReserva().getIdPolitica(),
                reserva.getEvento() == null ? null : reserva.getEvento().getIdEvento(),
                reserva.getNumeroComensales(),
                reserva.getHoraInicio(),
                reserva.getHoraFin(),
                reserva.getEstadoReserva(),
                reserva.getMotivoCancelacion(),
                reserva.getFechaCreacion(),
                reservaMesaRepository.findByReserva_IdReserva(reserva.getIdReserva())
                        .stream().map(item -> item.getMesa().getIdMesa()).toList()
        );
    }
}
