package com.axe.common.agGrid.filter;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum FilterType {
                  /** Equals equals Text, Number, Date */
                  equals("equals"),
                  /** Not Equals notEqual Text, Number, Date */
                  notEqual("notEqual"),
                  /** Contains contains Text */
                  contains("contains"),
                  /** Not Contains notContains Text */
                  notContains("notContains"),
                  /** Starts With startsWith Text */
                  startsWith("startsWith"),
                  /** Ends With endsWith Text */
                  endsWith("endsWith"),
                  /** Less Than lessThan Number, Date */
                  lessThan("lessThan"),
                  /** Less Than or Equal lessThanOrEqual Number */
                  lessThanOrEqual("lessThanOrEqual"),
                  /** Greater Than greaterThan Number, Date */
                  greaterThan("greaterThan"),
                  /** Greater Than or Equal greaterThanOrEqual Number */
                  greaterThanOrEqual("greaterThanOrEqual"),
                  /** In Range inRange Number, Date */
                  inRange("inRange"),
                  /** Empty* empty Text, Number, Date */
                  empty("empty"),
                  /** Blank* blank Text, Number, Date */
                  blank("blank"),
                  /** Not Blank* notBlank Text, Number, Date */
                  notBlank("notBlank"),
                  
                  /** Before Date */
                  before("before"),
                  /** After Date */
                  after("after"),
                  ;

   private final String value;

   FilterType(final String newValue) {
      value = newValue;
   }

   public static FilterType fromValue(final String value)
   {
      return Arrays.stream(FilterType.values()).filter(status -> status.value.equals(value)).findFirst().orElseThrow(NoSuchElementException::new);
   }

   public String getValue()
   {
      return value;
   }
}
