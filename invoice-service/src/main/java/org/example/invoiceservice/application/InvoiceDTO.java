package org.example.invoiceservice.application;

import java.util.UUID;

public record InvoiceDTO (UUID invoiceId,UUID customerId, Double amount, String currency) {
}
