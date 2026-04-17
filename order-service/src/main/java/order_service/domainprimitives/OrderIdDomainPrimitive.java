package order_service.domainprimitives;

import lombok.*;
import order_service.domain.OrderException;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderIdDomainPrimitive {

    private UUID orderId;

    protected OrderIdDomainPrimitive(UUID orderId) {
        if(orderId == null){ throw new OrderException("Order id is null"); }
        this.orderId = orderId;
    }

    public static OrderIdDomainPrimitive of(UUID orderId) {
        return new OrderIdDomainPrimitive(orderId);
    }
}
