package invoice_service.invoice.domain;

import invoice_service.IMSAbstractException;
import org.springframework.http.HttpStatus;

public class InvoiceException extends IMSAbstractException {
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String ERROR_CODE = "CUSTOMER_NOT_FOUND";

    public InvoiceException(String customMessage) {
        super(STATUS, customMessage, ERROR_CODE);
    }
}