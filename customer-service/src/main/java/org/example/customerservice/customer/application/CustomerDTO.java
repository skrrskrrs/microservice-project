package org.example.customerservice.customer.application;

import org.example.customerservice.customer.domainprimitives.CustomerId;
import org.example.customerservice.customer.domainprimitives.HomeAddress;
import org.example.customerservice.customer.domainprimitives.MailAddress;

import java.util.UUID;


public record CustomerDTO(UUID customerId, String firstName, String lastName, String mailAddress, String street, String city, String state, String zip) {
}
