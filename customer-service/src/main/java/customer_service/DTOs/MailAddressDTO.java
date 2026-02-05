package customer_service.DTOs;

import customer_service.customer.domainprimitives.MailAddress;

public record MailAddressDTO (String mailAddress) {

    public static  MailAddressDTO mailAddressAsDTO(MailAddress mailAddress) {
        return new MailAddressDTO(mailAddress.getMailAddress());
    }
}
