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
public class UserNameDomainPrimitive {
    private String userName;

    protected UserNameDomainPrimitive(String userName) {
        if(userName == null || userName.isBlank()) throw new UserException("Username is null or empty");
        this.userName = userName;
    }

    public static UserNameDomainPrimitive of(String userName) {
        return new UserNameDomainPrimitive(userName);
    }
}
