package invoice_service.invoice.domain;

import invoice_service.IMSAbstractException;

public class InvoiceException extends IMSAbstractException {
    public InvoiceException(String message) {
        super(message);
    }
}