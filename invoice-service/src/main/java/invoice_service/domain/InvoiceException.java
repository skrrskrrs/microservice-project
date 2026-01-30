package invoice_service.domain;

import invoice_service.IMSAbstractException;

public class InvoiceException extends IMSAbstractException {
    public InvoiceException(String message) {
        super(message);
    }
}