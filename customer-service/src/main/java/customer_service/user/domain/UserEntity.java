package customer_service.user.domain;

import customer_service.user.domainprimitives.HashedPasswordDomainPrimitive;
import customer_service.user.domainprimitives.UserId;
import customer_service.user.domainprimitives.UserNameDomainPrimitive;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserEntity {
    @EmbeddedId
    private UserId id;

    @Embedded
    @AttributeOverride(name = "userName", column = @Column(name = "username", unique = true, nullable = false))
    private UserNameDomainPrimitive userName;
    @Embedded
    private HashedPasswordDomainPrimitive hashedPassword;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    private boolean enabled = true;
}
