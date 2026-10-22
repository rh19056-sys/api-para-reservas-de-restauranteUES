package sv.edu.ues.reservas.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.reservas.domain.entity.Reserva;
import sv.edu.ues.reservas.domain.enums.EstadoReserva;
import sv.edu.ues.reservas.repository.ReservaRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaValidatorTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaValidator validator;

    private final UUID idReserva = UUID.randomUUID();

    @Test
    void reservaCompletadaAdmitePagoPeroNoConsumos() {
        stubReserva(EstadoReserva.COMPLETADA);

        assertDoesNotThrow(() -> validator.obtenerParaPago(idReserva));
        assertThrows(IllegalArgumentException.class,
                () -> validator.obtenerParaConsumo(idReserva));
    }

    @Test
    void reservaCanceladaNoAdmitePagos() {
        stubReserva(EstadoReserva.CANCELADA);

        assertThrows(IllegalArgumentException.class,
                () -> validator.obtenerParaPago(idReserva));
    }

    private void stubReserva(EstadoReserva estado) {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(idReserva);
        reserva.setEstadoReserva(estado);
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));
    }
}
