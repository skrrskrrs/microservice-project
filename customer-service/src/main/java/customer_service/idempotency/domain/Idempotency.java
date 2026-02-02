package customer_service.idempotency.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Idempotency {
    @Id
    private UUID id;

    protected Idempotency(UUID id) {
        this.id = id;
    }

    public static Idempotency newInstance(UUID id) {
        return new Idempotency(id);
    }
}
