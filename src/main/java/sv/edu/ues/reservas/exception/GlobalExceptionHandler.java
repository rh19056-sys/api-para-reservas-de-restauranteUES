package sv.edu.ues.reservas.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> manejarIntegridad(
            DataIntegrityViolationException ex
    ) {
        logger.warn("Operación rechazada por una restricción de integridad de datos", ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        new ApiError(
                                OffsetDateTime.now(),
                                409,
                                "DATA_INTEGRITY_CONFLICT",
                                "La operación entra en conflicto con datos relacionados",
                                List.of()
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarErrorInterno(
            Exception ex
    ) {

        logger.error("Error interno no controlado durante una solicitud HTTP", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ApiError(
                                OffsetDateTime.now(),
                                500,
                                "INTERNAL_SERVER_ERROR",
                                "Ocurrió un error interno en el servidor",
                                List.of()
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