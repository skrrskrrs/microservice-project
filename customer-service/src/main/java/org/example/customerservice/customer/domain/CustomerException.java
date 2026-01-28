package org.example.customerservice.customer.domain;

import org.example.customerservice.IMSAbstractException;

public class CustomerException extends IMSAbstractException {
    public CustomerException(String message) {
        super(message);
    }
}
