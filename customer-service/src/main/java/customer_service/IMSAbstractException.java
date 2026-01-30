package customer_service;

public abstract class IMSAbstractException extends RuntimeException {
    public IMSAbstractException( String message ) {
        super( message );
    }
}

