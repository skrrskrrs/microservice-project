package payment_service.payment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DialectOverride;
import payment_service.DTOs.PaymentDTO;
import payment_service.payment.domainprimitives.MoneyDomainPrimitive;
import payment_service.payment.domainprimitives.PaymentId;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @EmbeddedId
    private PaymentId id;
    @Embedded
    private MoneyDomainPrimitive money;
    //TODO OrderId erstellen
    private UUID orderId;
    private boolean isPaid;
    @Version
    private Long version;

    protected Payment(MoneyDomainPrimitive money, UUID orderId) {
        this.id = PaymentId.of(UUID.randomUUID());
        this.orderId = orderId;
        this.money = money;
        this.isPaid = false;
    }

    public static Payment createPayment(MoneyDomainPrimitive money, UUID orderId) {
        return new Payment(money, orderId);
    }

    public void confirmPayment(MoneyDomainPrimitive money){
        if(this.isPaid){
            throw new PaymentException("Payment is already paid");
        }
        if(!this.money.equals(money)){
            throw new PaymentException("Mismatch between payment and money");
        }
        this.isPaid = true;
    }
}
