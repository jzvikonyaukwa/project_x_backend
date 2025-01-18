package com.axe.common.converter;

import com.axe.common.enums.UserGroup;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserGroupConverter implements AttributeConverter<UserGroup, String> {

   @Override
   public String convertToDatabaseColumn(final UserGroup attribute) {
      if (attribute == null) {
         return null;
      }

      switch (attribute) {
         case opkiDevelopersGroup:
            return UserGroup.opkiDevelopersGroup.getValue();

         case managersGroup:
            return UserGroup.managersGroup.getValue();

         case superUserGroup:
            return UserGroup.superUserGroup.getValue();

         default:
            throw new IllegalArgumentException(String.format("%s not supported.", attribute));
      }
   }

   @Override
   public UserGroup convertToEntityAttribute(final String dbData) {
      if (dbData == null) {
         return null;
      }

      switch (dbData.toLowerCase()) {
         case "opkidevelopersgroup":
            return UserGroup.opkiDevelopersGroup;

         case "managersgroup":
            return UserGroup.managersGroup;

         case "superusergroup":
            return UserGroup.superUserGroup;

         default:
            throw new IllegalArgumentException(String.format("%s not supported.", dbData));
      }
   }

}
