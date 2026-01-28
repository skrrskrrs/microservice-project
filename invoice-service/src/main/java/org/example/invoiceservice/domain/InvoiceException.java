package org.example.invoiceservice.domain;

import org.example.invoiceservice.IMSAbstractException;

public class InvoiceException extends IMSAbstractException {
    public InvoiceException(String message) {
        super(message);
    }
}