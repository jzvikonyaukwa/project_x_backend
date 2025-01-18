package com.axe.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SaleOrderStatus {
   /* open */
   open("open"),
   /* completed */
   completed("completed"),
   /* cancelled */
   cancelled("cancelled"),
   ;

   private final String value$;

   SaleOrderStatus(final String value) {
      this.value$ = value;
   }

   @JsonProperty("status")
   public String getValue() {
      return value$;
   }

   public static SaleOrderStatus fromValue(String value) {
      for (SaleOrderStatus status : SaleOrderStatus.values()) {
         if (status.value$.equalsIgnoreCase(value)) {
            return status;
         }
      }
      throw new IllegalArgumentException("Unknown enum type " + value);
   }
}
