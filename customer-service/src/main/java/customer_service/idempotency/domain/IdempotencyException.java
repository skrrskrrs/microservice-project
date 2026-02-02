package customer_service.idempotency.domain;

import customer_service.IMSAbstractException;
import org.springframework.http.HttpStatus;

public class IdempotencyException extends IMSAbstractException {
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    private static final String ERROR_CODE = "IDEMPOTENCY_EXCEPTION";

    public IdempotencyException(String message) {
        super(STATUS,message,ERROR_CODE);
    }
}
