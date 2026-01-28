package org.example.invoiceservice.domain;

import org.example.invoiceservice.domainprimitives.CustomerId;
import org.example.invoiceservice.domainprimitives.InvoiceId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InvoiceRepository extends CrudRepository<Invoice, InvoiceId> {
    List<Invoice> findByCustomerId(CustomerId customerId);
}
