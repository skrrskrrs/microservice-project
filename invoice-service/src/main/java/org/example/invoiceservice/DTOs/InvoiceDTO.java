package org.example.invoiceservice.DTOs;

import java.util.UUID;

public record InvoiceDTO (UUID invoiceId,UUID customerId, Double amount, String currency) {
}
