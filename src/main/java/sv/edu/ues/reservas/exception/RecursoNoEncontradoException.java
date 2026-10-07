package sv.edu.ues.reservas.exception;

public class RecursoNoEncontradoException
        extends RuntimeException {

    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}