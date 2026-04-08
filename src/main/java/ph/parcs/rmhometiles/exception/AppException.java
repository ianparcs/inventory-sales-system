package ph.parcs.rmhometiles.exception;

public class AppException extends Exception {

    public AppException(ErrorCode errorMessage) {
        super(errorMessage.getTypeValue());
    }
}
