package org.example.customerservice;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.customerservice.customer.domainprimitives.CustomerId;

import java.util.UUID;

@Converter
public class CustomerIdConverter implements AttributeConverter<CustomerId, UUID> {
    @Override
    public UUID convertToDatabaseColumn(CustomerId attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public CustomerId convertToEntityAttribute(UUID dbData) {
        return dbData == null ? null : CustomerId.newInstance(dbData);
    }
}
