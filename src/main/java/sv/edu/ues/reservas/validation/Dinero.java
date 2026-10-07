package sv.edu.ues.reservas.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

// Importe compatible con NUMERIC(12,2): no negativo y máximo 2 decimales.
// Para exigir > 0 combinar con @Positive.
@DecimalMin(value = "0.00", message = "El importe no puede ser negativo")
@Digits(
        integer = 10,
        fraction = 2,
        message = "El importe admite máximo 10 enteros y 2 decimales"
)
@Constraint(validatedBy = {})
@Target({FIELD, PARAMETER, RECORD_COMPONENT})
@Retention(RUNTIME)
@Documented
public @interface Dinero {

    String message() default "Importe inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
