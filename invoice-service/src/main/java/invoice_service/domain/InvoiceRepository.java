package invoice_service.domain;

import invoice_service.domainprimitives.CustomerId;
import invoice_service.domainprimitives.InvoiceId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InvoiceRepository extends CrudRepository<Invoice, InvoiceId> {
    List<Invoice> findByCustomerId(CustomerId customerId);
}
