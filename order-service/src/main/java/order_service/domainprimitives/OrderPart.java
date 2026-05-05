package order_service.domainprimitives;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import order_service.domain.OrderException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Embeddable
public class OrderPart {
    private String productName;
    private int quantity;

    protected OrderPart( String productName, int quantity) {
        if(productName == null) throw new OrderException("Order name is null");
        if(quantity < 0) throw new OrderException("Quantity is negative");
        if(productName.isEmpty()) throw new OrderException("Product name cant be empty");
        this.productName = productName;
        this.quantity = quantity;
    }

    public static OrderPart of( String productName, int quantity) {
        return new OrderPart(productName, quantity);
    }
}
