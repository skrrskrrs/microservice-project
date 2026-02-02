package customer_service.idempotency.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IdempotencyRepository extends JpaRepository<Idempotency, UUID> {
    @Modifying
    @Query(
            value = "INSERT INTO idempotency (id) VALUES (:id) ON CONFLICT DO NOTHING",
            nativeQuery = true
    )
    int insertIfNotExists(@Param("id") UUID id);
}
