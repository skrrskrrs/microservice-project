package invoice_service.invoice.application;

import invoice_service.DTOs.InvoiceAndCustomerDTO;
import invoice_service.DTOs.InvoiceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
public class InvoiceRESTController {
    private final InvoiceApplicationService invoiceApplicationService;


    public InvoiceRESTController(InvoiceApplicationService invoiceApplicationService) {
        this.invoiceApplicationService = invoiceApplicationService;
    }

    @PostMapping("/invoices")
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        InvoiceDTO newInvoice = invoiceApplicationService.createInvoice(invoiceDTO);
        URI returnURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand( newInvoice.invoiceId())
                .toUri();
        return ResponseEntity
                .created(returnURI)
                .body( newInvoice );
    }

    @GetMapping("/invoices/{invoiceId}")
    public ResponseEntity<InvoiceAndCustomerDTO> getInvoiceForCustomer(@PathVariable UUID invoiceId) {
        InvoiceAndCustomerDTO invoiceDTO = invoiceApplicationService.getInvoiceForCustomer(invoiceId);
        return new ResponseEntity<>(invoiceDTO, HttpStatus.OK);
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<InvoiceDTO>> getAllInvoicesForCustomer(@RequestParam UUID customerId) {
        List<InvoiceDTO> invoiceDTO = invoiceApplicationService.getListOfInvoicesForCustomer( customerId);
        return new ResponseEntity<>(invoiceDTO,HttpStatus.OK);
    }
}
