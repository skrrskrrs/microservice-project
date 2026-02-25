package customer_service.user.domainprimitives;

import customer_service.user.domain.UserException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Getter
@Embeddable
@EqualsAndHashCode
public class UserId {
    private UUID id;

    protected UserId(UUID id) {
        if(id == null ) throw new UserException("User ID cannot be null");
        this.id = UUID.randomUUID();
    }

    public static UserId of(UUID id) {
        return new UserId(id);
    }
}
