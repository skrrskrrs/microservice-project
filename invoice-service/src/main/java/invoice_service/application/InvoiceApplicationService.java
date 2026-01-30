package invoice_service.application;

import invoice_service.DTOs.CustomerDTO;
import invoice_service.DTOs.InvoiceAndCustomerDTO;
import invoice_service.DTOs.InvoiceDTO;
import invoice_service.components.CustomerClient;
import invoice_service.domain.Invoice;
import invoice_service.domain.InvoiceException;
import invoice_service.domain.InvoiceRepository;
import invoice_service.domainprimitives.CustomerId;
import invoice_service.domainprimitives.InvoiceId;
import invoice_service.domainprimitives.MoneyAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InvoiceApplicationService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerClient customerClient;

    @Autowired
    public InvoiceApplicationService(InvoiceRepository invoiceRepository, CustomerClient customerClient) {
        this.invoiceRepository = invoiceRepository;
        this.customerClient = customerClient;
    }

    public Invoice findInvoice(UUID invoiceId) {
        return invoiceRepository.findById(InvoiceId.newInstance(invoiceId))
                .orElseThrow(() -> new InvoiceException("Invoice doesnt exist"));
    }

    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        if (!customerClient.exists(invoiceDTO.customerId())) {
            throw new InvoiceException("Customer not found");
        }

        Invoice invoice = Invoice.newInstance(
                CustomerId.newInstance(invoiceDTO.customerId()),
                MoneyAmount.newInstance(invoiceDTO.amount(), invoiceDTO.currency())
        );
        invoiceRepository.save(invoice);

        return new InvoiceDTO(
                invoice.getId().getId(),
                invoice.getCustomerId().getId(),
                invoice.getMoneyAmount().getAmount(),
                invoice.getMoneyAmount().getCurrency());
    }

    public InvoiceAndCustomerDTO getInvoiceForCustomer(UUID invoiceId) {
        Invoice invoice = findInvoice(invoiceId);
        CustomerDTO customerDTO = customerClient.getCustomerById(invoice.getCustomerId().getId());
        return new InvoiceAndCustomerDTO(
                invoice.getId().getId(),
                customerDTO,
                invoice.getMoneyAmount().getAmount(),
                invoice.getMoneyAmount().getCurrency());
    }

    public List<InvoiceDTO> getListOfInvoicesForCustomer(UUID customerId) {
        return invoiceRepository
                .findByCustomerId(CustomerId.newInstance(customerId))
                .stream()
                .map(invoice -> new InvoiceDTO(
                        invoice.getId().getId(),
                        invoice.getCustomerId().getId(),
                        invoice.getMoneyAmount().getAmount(),
                        invoice.getMoneyAmount().getCurrency()
                ))
                .toList();
    }

}
