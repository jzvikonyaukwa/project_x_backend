package com.axe.common.agGrid.request;

import java.io.Serializable;
import java.util.Map;

import com.axe.common.agGrid.filter.ColumnFilter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest implements Serializable
{
   private Map<String, ColumnFilter> filterModel;
}
