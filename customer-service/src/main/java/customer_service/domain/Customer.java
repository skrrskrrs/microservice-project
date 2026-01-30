package customer_service.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import customer_service.domainprimitives.CustomerId;
import customer_service.domainprimitives.HomeAddress;
import customer_service.domainprimitives.MailAddress;

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

    protected Customer(String firstName, String lastName, MailAddress mailAddress, HomeAddress homeAddress) {
        if(firstName == null || lastName == null || mailAddress == null || homeAddress == null) throw new CustomerException("CustomerId and/or MailAddress are null");
        if(firstName.isEmpty() || lastName.isEmpty()) throw new CustomerException("First Name or Last Name are Empty");
        this.customerId = CustomerId.newInstance(UUID.randomUUID());
        this.firstName = firstName;
        this.lastName = lastName;
        this.mailAddress = mailAddress;
        this.homeAddress = homeAddress;
    }

    public static Customer newInstance (  String firstName, String lastName, MailAddress mailAddress, HomeAddress homeAddress) {
        return new Customer( firstName, lastName, mailAddress, homeAddress);
    }

    public void changeHomeAddress(HomeAddress homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void changeMailAddress(MailAddress mailAddress) {
        this.mailAddress = mailAddress;
    }


}
