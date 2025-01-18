package com.axe.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserGroup {
   /* OpkiDevelopers */
   opkiDevelopersGroup("OpkiDevelopersGroup"), 
   /* Managers */
   managersGroup("ManagersGroup"),
   /* SuperUserGroup */
   superUserGroup("SuperUserGroup"),
   ;

   private final String value$;

   UserGroup(final String value) {
      this.value$ = value;
   }

   @JsonProperty("status")
   public String getValue() {
      return value$;
   }

   public static UserGroup fromValue(String value) {
      for (UserGroup status : UserGroup.values()) {
         if (status.value$.equalsIgnoreCase(value)) {
            return status;
         }
      }
      throw new IllegalArgumentException("Unknown enum type " + value);
   }
}
