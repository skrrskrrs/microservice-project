package customer_service.idempotency.application;

import customer_service.idempotency.domain.Idempotency;
import customer_service.idempotency.domain.IdempotencyException;
import customer_service.idempotency.domain.IdempotencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class IdempotencyApplicationService {
    private final IdempotencyRepository idempotencyRepository;

    @Autowired
    public IdempotencyApplicationService(IdempotencyRepository idempotencyRepository) {
        this.idempotencyRepository = idempotencyRepository;
    }

    public Idempotency findIdempotency(UUID idempotencyId){
        return idempotencyRepository.findById(idempotencyId).orElseThrow(()->new IdempotencyException("No such idempotency record"));
    }

    @Transactional
    public void ensureIdempotencyOnce(UUID id) {
        int inserted = idempotencyRepository.insertIfNotExists(id);
        if (inserted == 0) {
            throw new IdempotencyException("Already processed");
        }
    }
}
