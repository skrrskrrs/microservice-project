package payment_service.payment.domainprimitives;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import payment_service.payment.domain.PaymentException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class MoneyDomainPrimitive {

    private Long money;
    private String currency;

    protected MoneyDomainPrimitive(Long money, String currency) {
        if(money == null || currency == null || currency.isEmpty()) throw new PaymentException("Money or currency is null or empty");
        if(money < 0) throw new PaymentException("Money or currency is negative");
        if(!currency.equals("EUR") && !currency.equals("USD")) throw new PaymentException("Currency is not EUR or USD");
        this.money = money;
        this.currency = currency;
    }

    public static MoneyDomainPrimitive of(Long money, String currency) {
        return  new MoneyDomainPrimitive(money, currency);
    }
}
