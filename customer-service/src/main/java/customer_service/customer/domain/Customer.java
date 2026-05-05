package customer_service.customer.domain;

import customer_service.user.domainprimitives.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import customer_service.customer.domainprimitives.CustomerId;
import customer_service.customer.domainprimitives.HomeAddress;
import customer_service.customer.domainprimitives.MailAddress;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {
    @EmbeddedId
    private CustomerId customerId;
    private String firstName;
    private String lastName;
    @Embedded
    private MailAddress mailAddress;
    @Embedded
    private HomeAddress homeAddress;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "user_id")) // umbenannt
    })
    private UserId userId;

    private Customer(CustomerId id, String firstName, String lastName,
                     MailAddress mailAddress, HomeAddress homeAddress, UserId userId) {
        if (firstName == null || lastName == null || mailAddress == null || homeAddress == null)
            throw new CustomerException("Fields must not be null");

        if (firstName.isEmpty() || lastName.isEmpty())
            throw new CustomerException("First Name or Last Name are Empty");

        this.customerId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mailAddress = mailAddress;
        this.homeAddress = homeAddress;
        this.userId = userId;
    }


    public static Customer withId(CustomerId id, String firstName, String lastName, MailAddress mailAddress, HomeAddress homeAddress, UserId userId) {
        return new Customer(id, firstName, lastName, mailAddress, homeAddress, userId);
    }

    public static Customer of(String firstName, String lastName, MailAddress mailAddress, HomeAddress homeAddress, UserId userId) {
        return new Customer(CustomerId.of(UUID.randomUUID()),firstName, lastName, mailAddress, homeAddress, userId);
    }

    public void changeHomeAddress(HomeAddress homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void changeMailAddress(MailAddress mailAddress) {
        this.mailAddress = mailAddress;
    }

    public UUID getIdValue() {
        return this.customerId.getId();
    }

}
