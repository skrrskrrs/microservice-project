package org.example.customerservice.customer.domainprimitives;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.customerservice.customer.domain.CustomerException;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class MailAddress {
    private String mailAddress;

    protected MailAddress(String mailAddress) {
        if(mailAddress == null || mailAddress.isEmpty())throw new CustomerException("Mail address cannot be null or empty");
        if(!mailAddress.contains("@")) throw new CustomerException("Mail address must be an email address");
        this.mailAddress = mailAddress;
    }

    public static MailAddress newInstance(String mailAddress) {
        return new MailAddress(mailAddress);
    }
}
