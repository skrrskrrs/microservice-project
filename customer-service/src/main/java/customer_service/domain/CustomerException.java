package customer_service.domain;

import customer_service.IMSAbstractException;

public class CustomerException extends IMSAbstractException {
    public CustomerException(String message) {
        super(message);
    }
}
