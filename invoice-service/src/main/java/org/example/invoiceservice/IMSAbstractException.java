package org.example.invoiceservice;

public abstract class IMSAbstractException extends RuntimeException {
    public IMSAbstractException( String message ) {
        super( message );
    }
}

