package sv.edu.ues.reservas.dto.pago;

public record PagoActualizarRequest(

        String metodoPago,

        String estadoPago
) {
}

//--El monto y la reserva no se modifican: si están mal,
//--se rechaza el pago y se registra uno nuevo.
