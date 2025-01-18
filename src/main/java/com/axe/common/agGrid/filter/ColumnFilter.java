package com.axe.common.agGrid.filter;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.EqualsAndHashCode;

/**
 * Abstract base class for different types of column filters.
 * Uses Jackson annotations for polymorphic deserialization.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "filterType")
@JsonSubTypes({
            @JsonSubTypes.Type(value = NumberColumnFilter.class, name = "number"),
            @JsonSubTypes.Type(value = TextColumnFilter.class, name = "text"),
            @JsonSubTypes.Type(value = DateColumnFilter.class, name = "date"),
            @JsonSubTypes.Type(value = TimestampColumnFilter.class, name = "timestamp"),
            @JsonSubTypes.Type(value = SetColumnFilter.class, name = "set")
})
@EqualsAndHashCode
public abstract class ColumnFilter {
      /**
       * The type of the filter.
       */
      private String filterType;

      /**
       * Gets the filter type.
       * 
       * @return the filter type.
       */
      protected String getFilterType() {
            return filterType;
      }

      /**
       * Sets the filter type.
       * 
       * @param filterType the filter type to set.
       */
      protected void setFilterType(String filterType) {
            this.filterType = filterType;
      }
}
