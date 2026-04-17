package payment_service.payment.domainprimitives;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import payment_service.payment.domain.PaymentException;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode
public class PaymentId {
    private UUID id;

    protected PaymentId(UUID id) {
        if (id == null) throw new PaymentException("Id cant be null");
        this.id = id;
    }

    public static PaymentId of(UUID id){
        return new PaymentId(id);
    }

}
