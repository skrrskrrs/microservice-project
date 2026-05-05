package order_service.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import order_service.domainprimitives.OrderId;
import order_service.domainprimitives.OrderPart;

import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @EmbeddedId
    private OrderId orderId;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<OrderPart> orderPartList;

    private Order(OrderId orderId,  List<OrderPart> orderPartList) {
        if(orderId == null ) throw new OrderException("Order has invalid null values");
        this.orderId = orderId;
        this.orderPartList = orderPartList;
    }


    public static Order of(OrderId id,List<OrderPart> orderPartList) {
        return new Order(id, orderPartList);
    }
}
