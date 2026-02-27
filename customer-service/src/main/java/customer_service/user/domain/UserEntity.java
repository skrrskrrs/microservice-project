package customer_service.user.domain;

import customer_service.user.domainprimitives.HashedPasswordDomainPrimitive;
import customer_service.user.domainprimitives.UserId;
import customer_service.user.domainprimitives.UserNameDomainPrimitive;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

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
    private boolean isEnabled;

    protected UserEntity(UserNameDomainPrimitive userName, HashedPasswordDomainPrimitive hashedPassword, Set<Role> roles, boolean isEnabled) {
        if(id==null || userName==null || hashedPassword==null || roles==null) throw new UserException("Id, username, hashedPassword, roles is null");
        this.id = UserId.of(UUID.randomUUID());
        this.userName = userName;
        this.hashedPassword = hashedPassword;
        this.roles = roles;
        this.isEnabled = isEnabled;
    }

    public static UserEntity createWithRoles( UserNameDomainPrimitive userName, HashedPasswordDomainPrimitive hashedPassword, Set<Role> roles) {
        return new UserEntity(userName, hashedPassword, roles, true);
    }

    public static UserEntity registerNewUser(UserNameDomainPrimitive userName, HashedPasswordDomainPrimitive hashedPassword) {
        return new UserEntity(userName,hashedPassword,Set.of(Role.ROLE_USER),true);
    }
}
