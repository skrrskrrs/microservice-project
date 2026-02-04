package customer_service.DTOs;

import customer_service.customer.domainprimitives.HomeAddress;

public record HomeAddressDTO(String street, String city, String state, String zip) {
    public static HomeAddressDTO homeAddressAsDTO(HomeAddress homeAddress) {
        return new  HomeAddressDTO(homeAddress.getStreet(),  homeAddress.getCity(), homeAddress.getState(), homeAddress.getZip());
    }
}
