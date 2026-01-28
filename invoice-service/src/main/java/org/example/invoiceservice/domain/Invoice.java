package org.example.invoiceservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.invoiceservice.domainprimitives.CustomerId;
import org.example.invoiceservice.domainprimitives.InvoiceId;
import org.example.invoiceservice.domainprimitives.MoneyAmount;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invoice {
    @EmbeddedId
    @AttributeOverride(
            name = "id",
            column = @Column(name = "invoice_id")
    )
    private InvoiceId id;

    @Embedded
    private MoneyAmount moneyAmount;

    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "customer_id")
    )
    private CustomerId customerId;

    protected Invoice(CustomerId customerId, MoneyAmount moneyAmount) {
        if(customerId == null || moneyAmount == null) throw new InvoiceException("Invoice id , moneyamount or customer id cannot be null");
        this.id = InvoiceId.newInstance(UUID.randomUUID());
        this.customerId = customerId;
        this.moneyAmount = moneyAmount;
    }

    public static Invoice newInstance( CustomerId customerId,  MoneyAmount moneyAmount) {
        return new Invoice(customerId, moneyAmount);
    }


}
