package invoice_service.invoice.domain;

import invoice_service.invoice.domainprimitives.CustomerId;
import invoice_service.invoice.domainprimitives.InvoiceId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InvoiceRepository extends CrudRepository<Invoice, InvoiceId> {
    List<Invoice> findByCustomerId(CustomerId customerId);
}
