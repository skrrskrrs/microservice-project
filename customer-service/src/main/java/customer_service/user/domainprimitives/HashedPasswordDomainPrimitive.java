package customer_service.user.domainprimitives;

import customer_service.customer.domain.CustomerException;
import customer_service.user.domain.UserException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class HashedPasswordDomainPrimitive {
    private String hashedPassword;

    protected HashedPasswordDomainPrimitive(String hashedPassword) {
        if(hashedPassword == null || hashedPassword.isBlank()) throw new UserException("password cannot be null or empty");
        this.hashedPassword = hashedPassword;
    }

    public static HashedPasswordDomainPrimitive of(String hashedPassword) {
        return new HashedPasswordDomainPrimitive(hashedPassword);
    }
}
