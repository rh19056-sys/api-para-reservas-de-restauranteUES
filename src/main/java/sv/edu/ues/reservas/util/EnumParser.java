package sv.edu.ues.reservas.util;

import org.springframework.util.StringUtils;

// Convierte el texto recibido en un DTO al enum correspondiente.
// Ignora mayúsculas/espacios y lanza IllegalArgumentException (400)
// con un mensaje legible si el valor no existe.
public final class EnumParser {

    private EnumParser() {
    }

    public static <E extends Enum<E>> E parse(
            Class<E> tipo,
            String valor,
            String campo
    ) {

        if (!StringUtils.hasText(valor)) {
            throw new IllegalArgumentException(
                    campo + " es obligatorio"
            );
        }

        try {
            return Enum.valueOf(tipo, valor.trim().toUpperCase());

        } catch (IllegalArgumentException ex) {

            throw new IllegalArgumentException(
                    campo + " inválido: " + valor
            );
        }
    }

    public static <E extends Enum<E>> E parseOrDefault(
            Class<E> tipo,
            String valor,
            String campo,
            E porDefecto
    ) {

        if (!StringUtils.hasText(valor)) {
            return porDefecto;
        }

        return parse(tipo, valor, campo);
    }
}
