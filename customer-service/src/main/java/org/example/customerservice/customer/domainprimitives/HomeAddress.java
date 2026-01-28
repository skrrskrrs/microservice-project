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
public class HomeAddress {
    private String street;
    private String city;
    private String state;
    private String zip;

    protected HomeAddress(String street, String city, String state, String zip) {
        if(street==null || city==null || state==null || zip==null) throw new CustomerException("Address cannot be null");
        if(street.isEmpty() || city.isEmpty() || state.isEmpty() || zip.isEmpty()) throw new CustomerException("Address cannot be empty");
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public static HomeAddress newInstance(String street, String city, String state, String zip) {
        return new HomeAddress(street, city, state, zip);
    }

    @Override
    public String toString() {
        return "HomeAddress{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
