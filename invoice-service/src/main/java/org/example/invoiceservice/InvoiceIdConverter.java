package org.example.invoiceservice;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.invoiceservice.domainprimitives.InvoiceId;

import java.util.UUID;

@Converter
public class InvoiceIdConverter implements AttributeConverter<InvoiceId, UUID> {
    @Override
    public UUID convertToDatabaseColumn(InvoiceId attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public InvoiceId convertToEntityAttribute(UUID dbData) {
        return dbData == null ? null : InvoiceId.newInstance(dbData);
    }
}
