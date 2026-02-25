package customer_service.user.configuration;

import customer_service.customer.domainprimitives.CustomerId;
import customer_service.user.domainprimitives.UserId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

@Converter
public class UserIdConverter implements AttributeConverter<UserId, UUID> {
    @Override
    public UUID convertToDatabaseColumn(UserId attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public UserId convertToEntityAttribute(UUID dbData) {
        return dbData == null ? null : UserId.of(dbData);
    }
}