package invoice_service.DTOs;

import java.util.UUID;

public record InvoiceAndCustomerDTO (UUID invoiceId, CustomerDTO customerDTO, Double amount, String currency) { }

