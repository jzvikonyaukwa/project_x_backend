package com.axe.common.agGrid.filter;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NumberColumnFilter extends ColumnFilter implements Serializable
{
   private String type;
   private Number filter;
   private Number filterTo;
}
