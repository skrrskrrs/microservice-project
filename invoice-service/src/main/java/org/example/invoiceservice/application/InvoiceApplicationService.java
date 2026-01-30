package org.example.invoiceservice.application;

import org.example.invoiceservice.DTOs.CustomerDTO;
import org.example.invoiceservice.DTOs.InvoiceAndCustomerDTO;
import org.example.invoiceservice.DTOs.InvoiceDTO;
import org.example.invoiceservice.components.CustomerClient;
import org.example.invoiceservice.domain.Invoice;
import org.example.invoiceservice.domain.InvoiceException;
import org.example.invoiceservice.domain.InvoiceRepository;
import org.example.invoiceservice.domainprimitives.CustomerId;
import org.example.invoiceservice.domainprimitives.InvoiceId;
import org.example.invoiceservice.domainprimitives.MoneyAmount;
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
