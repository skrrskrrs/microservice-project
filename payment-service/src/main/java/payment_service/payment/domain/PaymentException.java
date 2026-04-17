package payment_service.payment.domain;

import org.springframework.http.HttpStatus;
import payment_service.IMSAbstractException;

public class PaymentException extends IMSAbstractException {
        private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
        private static final String ERROR_CODE = "CUSTOMER_NOT_FOUND";

        public PaymentException(String customMessage) {
            super(STATUS, customMessage, ERROR_CODE);
        }
    }

