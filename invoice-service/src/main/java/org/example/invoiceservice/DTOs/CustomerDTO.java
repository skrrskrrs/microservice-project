package org.example.invoiceservice.DTOs;

import java.util.UUID;

public record CustomerDTO(UUID customerId, String firstName, String lastName, String mailAddress, String street, String city, String state, String zip) {
}

