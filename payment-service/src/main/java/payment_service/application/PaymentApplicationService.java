package payment_service.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import payment_service.payment.domain.PaymentRepository;

@Service
public class PaymentApplicationService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentApplicationService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
