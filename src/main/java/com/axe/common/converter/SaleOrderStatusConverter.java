package com.axe.common.converter;

import com.axe.common.enums.SaleOrderStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SaleOrderStatusConverter implements AttributeConverter<SaleOrderStatus, String> {

   @Override
   public String convertToDatabaseColumn(final SaleOrderStatus attribute) {
      if (attribute == null) {
         return null;
      }

      switch (attribute) {
         case open:
            return SaleOrderStatus.open.getValue();

         case completed:
            return SaleOrderStatus.completed.getValue();

         case cancelled:
               return SaleOrderStatus.cancelled.getValue();

         default:
            throw new IllegalArgumentException(String.format("%s not supported.", attribute));
      }
   }

   @Override
   public SaleOrderStatus convertToEntityAttribute(final String dbData) {
      if (dbData == null) {
         return null;
      }

      switch (dbData.toLowerCase()) {
         case "open":
            return SaleOrderStatus.open;

         case "completed":
            return SaleOrderStatus.completed;

         case "cancelled":
            return SaleOrderStatus.cancelled;

         default:
            throw new IllegalArgumentException(String.format("%s not supported.", dbData));
      }
   }

}
