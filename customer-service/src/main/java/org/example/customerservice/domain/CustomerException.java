package org.example.customerservice.domain;

import org.example.customerservice.IMSAbstractException;

public class CustomerException extends IMSAbstractException {
    public CustomerException(String message) {
        super(message);
    }
}
