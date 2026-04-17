package order_service.domain;

import order_service.IMSAbstractException;
import org.springframework.http.HttpStatus;

public class OrderException extends IMSAbstractException {
  private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
  private static final String ERROR_CODE = "CUSTOMER_NOT_FOUND";

  public OrderException(String customMessage) {
    super(STATUS, customMessage, ERROR_CODE);
  }
}
