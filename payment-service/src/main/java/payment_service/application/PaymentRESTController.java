package payment_service.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentRESTController {

    private PaymentApplicationService paymentApplicationService;

    @Autowired
    public PaymentRESTController(PaymentApplicationService paymentApplicationService) {
        this.paymentApplicationService = paymentApplicationService;
    }
}
