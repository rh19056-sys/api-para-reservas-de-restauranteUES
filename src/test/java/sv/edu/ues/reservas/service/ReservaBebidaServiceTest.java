package sv.edu.ues.reservas.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.reservas.domain.entity.Bebida;
import sv.edu.ues.reservas.domain.entity.Reserva;
import sv.edu.ues.reservas.domain.entity.ReservaBebida;
import sv.edu.ues.reservas.domain.entity.RestauranteSucursal;
import sv.edu.ues.reservas.domain.enums.EstadoReserva;
import sv.edu.ues.reservas.dto.consumo.ConsumoCrearRequest;
import sv.edu.ues.reservas.repository.BebidaRepository;
import sv.edu.ues.reservas.repository.ReservaBebidaRepository;
import sv.edu.ues.reservas.repository.ReservaRepository;
import sv.edu.ues.reservas.validation.ReservaValidator;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaBebidaServiceTest {

    @Mock
    private ReservaBebidaRepository reservaBebidaRepository;
    @Mock
    private BebidaRepository bebidaRepository;
    @Mock
    private ReservaRepository reservaRepository;

    private ReservaBebidaService service;

    private final UUID idReserva = UUID.randomUUID();
    private final UUID idBebida = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new ReservaBebidaService(
                reservaBebidaRepository,
                bebidaRepository,
                new ReservaValidator(reservaRepository)
        );

        RestauranteSucursal sucursal = new RestauranteSucursal();
        sucursal.setIdSucursal(UUID.randomUUID());

        Reserva reserva = new Reserva();
        reserva.setIdReserva(idReserva);
        reserva.setSucursal(sucursal);
        reserva.setEstadoReserva(EstadoReserva.CONFIRMADA);

        Bebida bebida = new Bebida();
        bebida.setIdBebida(idBebida);
        bebida.setSucursal(sucursal);

        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));
        when(bebidaRepository.findById(idBebida)).thenReturn(Optional.of(bebida));
        when(reservaBebidaRepository.existsById(any())).thenReturn(false);
    }

    @Test
    void bebidaExigePrecioPorqueNoTienePrecioEnCatalogo() {
        assertThrows(IllegalArgumentException.class,
                () -> service.agregar(idReserva, new ConsumoCrearRequest(idBebida, 2, null)));
        verify(reservaBebidaRepository, never()).save(any(ReservaBebida.class));
    }

    @Test
    void bebidaUsaPrecioEnviado() {
        when(reservaBebidaRepository.save(any(ReservaBebida.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.agregar(
                idReserva, new ConsumoCrearRequest(idBebida, 2, new BigDecimal("3.25")));

        assertEquals(0, new BigDecimal("3.25").compareTo(response.precioUnitario()));
    }
}
