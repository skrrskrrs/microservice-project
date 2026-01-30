package invoice_service.DTOs;

import java.util.UUID;

public record InvoiceDTO (UUID invoiceId,UUID customerId, Double amount, String currency) {
}
