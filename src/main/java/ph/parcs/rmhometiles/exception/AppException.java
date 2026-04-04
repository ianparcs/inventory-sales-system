package ph.parcs.rmhometiles.exception;

public class AppException extends Exception {

    public AppException(ExceptionType errorMessage) {
        super(errorMessage.getTypeValue());
    }
}
