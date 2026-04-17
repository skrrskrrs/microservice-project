package order_service.domainprimitives;

import jakarta.persistence.Embeddable;
import lombok.*;
import order_service.domain.OrderException;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OrderId {

    private UUID orderId;

    protected OrderId(UUID orderId) {
        if(orderId == null){ throw new OrderException("Order id is null"); }
        this.orderId = orderId;
    }

    public static OrderId of(UUID orderId) {
        return new OrderId(orderId);
    }
}
