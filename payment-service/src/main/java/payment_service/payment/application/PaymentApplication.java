package payment_service.payment.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment_service.DTOs.PaymentDTO;
import payment_service.payment.domain.Payment;
import payment_service.payment.domain.PaymentException;
import payment_service.payment.domain.PaymentRepository;
import payment_service.payment.domainprimitives.MoneyDomainPrimitive;
import payment_service.payment.domainprimitives.PaymentId;

@Service
@Transactional
public class PaymentApplication {

    private PaymentRepository paymentRepository;

    public PaymentApplication(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    //Wenn Order erstellt wird, wird diese Funktion aufgerufen, noch ohne Bezahlung kann aber auch direkt bezahlt werden
    public void createNewPaymentFromOrder(MoneyDomainPrimitive moneyDomainPrimitive, OrderId orderId) {
        Payment newPayment = Payment.createPayment(moneyDomainPrimitive, orderId);
        paymentRepository.save(newPayment);
    }

    //Nachbezahlen
    public void payOrder(PaymentDTO paymentDTO) {
        Payment payment = paymentRepository.findByOrderId(paymentDTO.orderId()).orElseThrow(() -> new PaymentException("Payment not found"));
        //   int markAsPaid = paymentRepository.markAsPaid(PaymentId.of(payment.getId().getId()),MoneyDomainPrimitive.of(paymentDTO.money(),paymentDTO.currency()));
        //    if(markAsPaid == 0){
        //        throw new PaymentException("Payment already completed or amount mismatch");
        //    }
        payment.confirmPayment(MoneyDomainPrimitive.of(paymentDTO.money(), paymentDTO.currency()));
        paymentRepository.save(payment);
    }
}
