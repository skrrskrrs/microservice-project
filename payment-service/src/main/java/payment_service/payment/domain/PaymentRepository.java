package payment_service.payment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import payment_service.payment.domainprimitives.MoneyDomainPrimitive;
import payment_service.payment.domainprimitives.PaymentId;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, PaymentId> {
    Optional<Payment> findByOrderId(UUID orderId);
    @Modifying
    @Query("""
    UPDATE Payment p
    SET p.isPaid = true
    WHERE p.id = :id
      AND p.isPaid = false
      AND p.money = :money
""")
    int markAsPaid(@Param("id") PaymentId id, @Param("money") MoneyDomainPrimitive money);
}
