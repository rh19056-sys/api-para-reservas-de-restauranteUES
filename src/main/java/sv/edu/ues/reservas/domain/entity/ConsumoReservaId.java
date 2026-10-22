package sv.edu.ues.reservas.domain.entity;

import java.util.UUID;

// PK compuesta (id_reserva, id_<producto>) de los consumos de una reserva.
public interface ConsumoReservaId {

    UUID getIdReserva();

    UUID idProducto();
}
