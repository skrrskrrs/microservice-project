package order_service.domainprimitives;

import jakarta.persistence.Embeddable;
import lombok.*;
import order_service.order.OrderException;

import java.util.UUID;

@Getter
@EqualsAndHashCode( onlyExplicitlyIncluded = true )
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OrderId {
    @EqualsAndHashCode.Include
    private UUID orderId;

    protected OrderId(UUID orderId) {
        if(orderId == null){ throw new OrderException("Order id cant be null"); }
        this.orderId = orderId;
    }

    public static OrderId of(UUID orderId) {
        return new OrderId(orderId);
    }
}
