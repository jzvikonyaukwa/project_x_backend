package com.axe.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static org.hibernate.internal.util.StringHelper.isEmpty;

@Converter
public final class TrimConverter implements AttributeConverter<String, String> {

   @Override
   public String convertToDatabaseColumn(final String entityAttributeValue) {
      if (isEmpty(entityAttributeValue)) {
         return null;
      }

      return entityAttributeValue.trim();
   }

   @Override
   public String convertToEntityAttribute(final String databaseColumnValue) {
      if (isEmpty(databaseColumnValue)) {
         return null;
      }

      return databaseColumnValue.trim();
   }
}
