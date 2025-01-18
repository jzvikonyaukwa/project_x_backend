package com.axe.common.agGrid.filter;

import java.io.Serializable;
import java.util.Set;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetColumnFilter extends ColumnFilter implements Serializable
{
   private Set<String> values;
}
