package com.axe.common.agGrid.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortModel implements Serializable
{
   private String colId;
   private String sort;
}
