package com.axe.common.agGrid.response;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServerSideGetRowsResponse<T> implements Serializable
{
   private T data;
   private int lastRow;
   private List<String> secondaryColumnFields;

   // public ServerSideGetRowsResponse() {
   // }
   // public void setSecondaryColumns(final List<String> secondaryColumnFields)
   // {
   // this.secondaryColumnFields = secondaryColumnFields;
   // }
}
