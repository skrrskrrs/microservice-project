package payment_service.payment.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import payment_service.DTOs.PaymentDTO;

import java.util.UUID;

@RestController
public class PaymentRESTController {

    private PaymentApplication paymentApplication;

    public PaymentRESTController(PaymentApplication paymentApplication) {
        this.paymentApplication = paymentApplication;
    }

    @PostMapping("payments")
    public ResponseEntity<PaymentDTO> createPayment (@RequestHeader("Idempotency-Key") UUID idempotencyId, PaymentDTO paymentDTO){

    }
}
