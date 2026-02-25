package customer_service.customer.domainprimitives;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import customer_service.customer.domain.CustomerException;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class CustomerId implements Serializable {
    private UUID id;

    protected CustomerId(UUID id) {
        if(id == null) throw new CustomerException("Customer id cannot be null");
        this.id = id;
    }

    public static CustomerId of(UUID customerId) {
        return new CustomerId(customerId);
    }
}
