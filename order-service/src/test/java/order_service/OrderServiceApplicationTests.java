package order_service;

import order_service.domain.Order;
import order_service.domainprimitives.OrderId;
import order_service.domainprimitives.OrderPart;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class OrderServiceApplicationTests {

    @Test
    void contextLoads() {
        OrderPart orderPart = OrderPart.of("test",1);
        List<OrderPart> orderParts = new ArrayList<>();
        orderParts.add(orderPart);

        Order order = Order.of(OrderId.of(UUID.randomUUID()), orderParts);
    }

}
