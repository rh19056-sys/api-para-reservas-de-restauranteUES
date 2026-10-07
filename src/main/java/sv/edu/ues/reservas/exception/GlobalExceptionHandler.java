package sv.edu.ues.reservas.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> manejarNoEncontrado(
            RecursoNoEncontradoException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ApiError(
                                OffsetDateTime.now(),
                                404,
                                "NOT_FOUND",
                                ex.getMessage(),
                                List.of()
                        )
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(
            MethodArgumentNotValidException ex
    ) {

        List<String> details =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error ->
                                error.getField()
                                        + ": "
                                        + error.getDefaultMessage()
                        )
                        .toList();

        return ResponseEntity
                .badRequest()
                .body(
                        new ApiError(
                                OffsetDateTime.now(),
                                400,
                                "VALIDATION_ERROR",
                                "Datos de entrada inválidos",
                                details
                        )
                );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> manejarArgumento(
            IllegalArgumentException ex
    ) {

        return ResponseEntity
                .badRequest()
                .body(
                        new ApiError(
                                OffsetDateTime.now(),
                                400,
                                "BAD_REQUEST",
                                ex.getMessage(),
                                List.of()
                        )
                );
    }

    // FK inexistente, UNIQUE o CHECK violado en PostgreSQL.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> manejarIntegridad(
            DataIntegrityViolationException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        new ApiError(
                                OffsetDateTime.now(),
                                409,
                                "CONFLICT",
                                "La operación viola una restricción de la base de datos",
                                List.of(ex.getMostSpecificCause().getMessage())
                        )
                );
    }

    public record ApiError(
            OffsetDateTime timestamp,
            int status,
            String error,
            String message,
            List<String> details
    ) {
    }
}