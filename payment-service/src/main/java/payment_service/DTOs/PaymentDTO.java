package payment_service.DTOs;

import java.util.UUID;

public record PaymentDTO (Long money, String currency, UUID orderId){
}
