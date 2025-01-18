package com.axe.common.converter;

import com.axe.delivery_notes.Status;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DeliveryNoteStatusConverter implements AttributeConverter<Status, Integer> {

   @Override
   public Integer convertToDatabaseColumn(final Status entityAttributeValue) {
      if (entityAttributeValue == null) {
         return null;
      }

      switch (entityAttributeValue) {
         case DELIVERED:
            return Integer.parseInt(Status.DELIVERED.getValue());

         default:
            throw new IllegalArgumentException(String.format("%s not supported.", entityAttributeValue));
      }
   }

   @Override
   public Status convertToEntityAttribute(final Integer databaseColumnValue) {
      if (databaseColumnValue == null) {
         return null;
      }

      switch (databaseColumnValue) {
         case 0:
            return Status.DELIVERED;

         default:
            throw new IllegalArgumentException(String.format("%s not supported.", databaseColumnValue));
      }
   }

}
