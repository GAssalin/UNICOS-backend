package br.com.unicos.core.base.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Conversor genérico para armazenar enums como texto no banco de dados.
 */
@Converter(autoApply = true)
public class EnumToStringConverter<E extends Enum<E>> implements AttributeConverter<E, String> {
    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        return null; // cada enum pode implementar sua própria factory se necessário
    }
}
