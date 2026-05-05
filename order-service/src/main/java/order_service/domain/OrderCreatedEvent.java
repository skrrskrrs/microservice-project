package order_service.domain;

import java.util.UUID;

public class OrderCreatedEvent {

    private final UUID orderId;

    public OrderCreatedEvent(UUID orderId) {
        this.orderId = orderId;
    }
}
