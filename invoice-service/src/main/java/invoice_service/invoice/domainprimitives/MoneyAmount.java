package invoice_service.invoice.domainprimitives;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import invoice_service.invoice.domain.InvoiceException;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class MoneyAmount {
    private Double amount;
    private String currency;

    protected MoneyAmount(Double amount, String currency) {
        if (currency == null || amount == null) throw new InvoiceException("currency or amount is null");
        if (amount < 0) throw new InvoiceException("amount is negative");
        if (currency.isEmpty()) throw new InvoiceException("currency is empty");
        this.amount = amount;
        this.currency = currency;
    }

    public static MoneyAmount newInstance(Double amount, String currency) {
        return new MoneyAmount(amount, currency);
    }


}
