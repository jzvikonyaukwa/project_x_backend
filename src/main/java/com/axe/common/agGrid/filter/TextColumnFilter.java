package com.axe.common.agGrid.filter;

import java.io.Serializable;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextColumnFilter extends ColumnFilter implements Serializable
{
   private String type;
   private String filter;

   //   public TextColumnFilter(final String type,final String filter) {
   //      this.type = type;
   //      this.filter = filter;
   //   }
}
