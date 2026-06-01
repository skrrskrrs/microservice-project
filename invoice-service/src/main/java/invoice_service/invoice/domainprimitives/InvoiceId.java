package invoice_service.invoice.domainprimitives;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import invoice_service.invoice.domain.InvoiceException;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode( onlyExplicitlyIncluded = true )
public class InvoiceId implements Serializable {
    @EqualsAndHashCode.Include
    private UUID id;

    protected InvoiceId(UUID id) {
        if (id == null) throw new InvoiceException("id is null");
        this.id = id;
    }

    public static InvoiceId newInstance(UUID id) {
        return new InvoiceId(id);
    }
}
