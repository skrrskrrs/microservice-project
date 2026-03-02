package customer_service.customer.domainprimitives;

import customer_service.customer.domain.CustomerException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@Getter
@EqualsAndHashCode
public class UserId {

    private UUID id;

    protected UserId(UUID id) {
        if(id == null) throw new CustomerException("Customer id cannot be null");
        this.id = id;
    }

    public static UserId of(UUID userId) {
        return new UserId(userId);
    }
}
