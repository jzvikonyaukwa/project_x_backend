package com.axe.common.agGrid.request;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.axe.common.agGrid.filter.ColumnFilter;

import lombok.Data;

@Data
public class ServerSideGetRowsRequest implements Serializable {
   private int startRow;
   private int endRow;
   private Map<String, ColumnFilter> filterModel;
   private List<SortModel> sortModel;
   private List<ColumnVO> rowGroupCols;
   private List<ColumnVO> pivotCols;
   private List<ColumnVO> valueCols;
   private List<String> groupKeys;
   private Boolean pivotMode = false;

   public ServerSideGetRowsRequest() {
      this.filterModel = emptyMap();
      this.sortModel = emptyList();
      this.rowGroupCols = emptyList();
      this.pivotCols = emptyList();
      this.valueCols = emptyList();
      this.groupKeys = emptyList();
   }

   /**
    * Calculates the number of rows requested.
    * @return the number of rows, or 0 if startRow is greater than endRow.
    */
   public int getRowCount() {
      int rowCount = this.endRow - this.startRow;
      return rowCount < 0 ? 0 : rowCount; // Ensure rowCount is non-negative
   }

   /**
    * Calculates the page number based on the startRow and rowCount.
    * @return the page number, or 0 if rowCount is zero.
    */
   public int getPageNumber() {
      int rowCount = getRowCount();
      return rowCount > 0 ? this.startRow / rowCount : 0; // Return 0 or an appropriate value if rowCount is zero
   }
}