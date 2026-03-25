package customer_service.user.domainprimitives;

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
public class PasswordDomainPrimitive {

    private String password;

    protected PasswordDomainPrimitive(String password) {
        if (password == null || password.isBlank()) throw new UserException("Password cannot be empty or null");

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#_-])[A-Za-z\\d@$!%*?&.#_-]{8,}$";
        if (!password.matches(regex)) {
            throw new UserException(
                    "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character."
            );
        }
        this.password = password;

    }

    public static PasswordDomainPrimitive of(String password) {
        return new PasswordDomainPrimitive(password);
    }
}
