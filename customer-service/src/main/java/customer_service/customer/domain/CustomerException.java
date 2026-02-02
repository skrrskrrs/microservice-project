package customer_service.customer.domain;

import customer_service.IMSAbstractException;
import org.springframework.http.HttpStatus;

public class CustomerException extends IMSAbstractException {
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String ERROR_CODE = "CUSTOMER_NOT_FOUND";

    public CustomerException(String customMessage) {
        super(STATUS, customMessage, ERROR_CODE);
    }
}
