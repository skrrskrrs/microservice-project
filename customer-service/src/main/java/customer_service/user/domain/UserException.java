package customer_service.user.domain;

import customer_service.IMSAbstractException;
import org.springframework.http.HttpStatus;

public class UserException extends IMSAbstractException {
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String ERROR_CODE = "USER_NOT_FOUND";

    public UserException(String message) {
        super(STATUS, message, ERROR_CODE);
    }
}
