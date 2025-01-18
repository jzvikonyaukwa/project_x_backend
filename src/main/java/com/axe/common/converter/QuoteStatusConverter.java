package com.axe.common.converter;

import com.axe.common.enums.QuoteStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class QuoteStatusConverter implements AttributeConverter<QuoteStatus, String> {

   @Override
   public String convertToDatabaseColumn(final QuoteStatus attribute) {
      if (attribute == null) {
         return null;
      }

      switch (attribute) {
         case draft:
            return QuoteStatus.draft.getValue();

         case pending_approval:
            return QuoteStatus.pending_approval.getValue();

         case approved:
            return QuoteStatus.approved.getValue();

         case accepted:
            return QuoteStatus.accepted.getValue();

         case rejected:
            return QuoteStatus.rejected.getValue();

         default:
            throw new IllegalArgumentException(String.format("%s not supported.", attribute));
      }
   }

   @Override
   public QuoteStatus convertToEntityAttribute(final String dbData) {
      if (dbData == null) {
         return null;
      }

      switch (dbData.toLowerCase()) {
         case "draft":
            return QuoteStatus.draft;

         case "pending_approval":
         case "pending approval":
            return QuoteStatus.pending_approval;

         case "approved":
            return QuoteStatus.approved;

         case "accepted":
            return QuoteStatus.accepted;

         case "rejected":
            return QuoteStatus.rejected;

         default:
            throw new IllegalArgumentException(String.format("%s not supported.", dbData));
      }
   }

}
