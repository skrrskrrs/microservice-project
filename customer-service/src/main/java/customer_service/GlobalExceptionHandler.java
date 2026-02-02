package customer_service;

import customer_service.customer.domain.CustomerException;
import customer_service.idempotency.domain.IdempotencyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IMSAbstractException.class)
    public ResponseEntity<?> handleCustomerNotFound(IMSAbstractException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(ex.getMessage());
    }
}