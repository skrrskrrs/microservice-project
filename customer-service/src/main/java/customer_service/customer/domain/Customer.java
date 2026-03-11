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

    protected Customer(String firstName, String lastName, MailAddress mailAddress, HomeAddress homeAddress,UserId userId ) {
        if (firstName == null || lastName == null || mailAddress == null || homeAddress == null)
            throw new CustomerException("CustomerId and/or MailAddress are null");
        if (firstName.isEmpty() || lastName.isEmpty()) throw new CustomerException("First Name or Last Name are Empty");
        this.customerId = CustomerId.of(UUID.randomUUID());
        this.firstName = firstName;
        this.lastName = lastName;
        this.mailAddress = mailAddress;
        this.homeAddress = homeAddress;
        this.userId = userId;
    }

    protected Customer(CustomerId id, String firstName, String lastName, MailAddress mailAddress, HomeAddress homeAddress, UserId userId) {
        if (firstName == null || lastName == null || mailAddress == null || homeAddress == null)
            throw new CustomerException("CustomerId and/or MailAddress are null");
        if (firstName.isEmpty() || lastName.isEmpty()) throw new CustomerException("First Name or Last Name are Empty");
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
        return new Customer(firstName, lastName, mailAddress, homeAddress, userId);
    }

    public void changeHomeAddress(HomeAddress homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void changeMailAddress(MailAddress mailAddress) {
        this.mailAddress = mailAddress;
    }


}
