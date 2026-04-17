package payment_service.payment.domainprimitives;
import jakarta.persistence.Embeddable;
import lombok.*;
import payment_service.payment.domain.PaymentException;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OrderId {

    private UUID orderId;

    protected OrderId(UUID orderId) {
        if(orderId == null){ throw new PaymentException("Order id is null"); }
        this.orderId = orderId;
    }

    public static OrderId of(UUID orderId) {
        return new OrderId(orderId);
    }
}
