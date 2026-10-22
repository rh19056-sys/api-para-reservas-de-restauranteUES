package sv.edu.ues.reservas.dto.comprobante;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record ComprobantePagoCrearRequest(

        @NotBlank(message = "El formato es obligatorio")
        String formato,

        @Size(
                max = 100,
                message = "El número de comprobante no puede superar 100 caracteres"
        )
        String numeroComprobante,

        @Size(
                max = 255,
                message = "La referencia no puede superar 255 caracteres"
        )
        String referencia,

        Map<String, Object> desglose
) {
}

//--Si no se envía numeroComprobante, el Service genera uno.
