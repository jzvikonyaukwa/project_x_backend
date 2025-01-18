package com.axe.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum QuoteStatus {
   /* DRAFT */
   draft("draft"),
   /* PENDING APPROVAL */
   pending_approval("pending approval"),
   /* APPROVED */
   approved("approved"),
   /* ACCEPTED */
   accepted("accepted"),
   /* REJECTED */
   rejected("rejected"),
   ;

   private final String value$;

   QuoteStatus(final String value) {
      this.value$ = value;
   }

   @JsonProperty("status")
   public String getValue() {
      return value$;
   }

   public static QuoteStatus fromValue(String value) {
      for (QuoteStatus status : QuoteStatus.values()) {
         if (status.value$.equalsIgnoreCase(value)) {
            return status;
         }
      }
      throw new IllegalArgumentException("Unknown enum type " + value);
   }
}
