package customer_service.user.domainprimitives;

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
        if(hashedPassword == null || hashedPassword.isBlank()) throw new IllegalArgumentException("password cannot be null or empty");
        if(hashedPassword.length() < 8) throw new IllegalArgumentException("password must be at least 8 characters");

        String regex =  "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";

        if(!hashedPassword.matches(regex)){
            throw new IllegalArgumentException("Password must contain upper, lower, digit and special character");
        }

        this.hashedPassword = hashedPassword;
    }

    public static HashedPasswordDomainPrimitive of(String hashedPassword) {
        return new HashedPasswordDomainPrimitive(hashedPassword);
    }
}
