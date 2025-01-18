package com.axe.common.agGrid.filter;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;

import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DateColumnFilter extends ColumnFilter implements Serializable
{
   @Setter
   private String type;
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.TRUE)
   @Setter(AccessLevel.PRIVATE)
   private LocalDateTime dateFrom;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.TRUE)
   @Setter(AccessLevel.PRIVATE)
   private LocalDateTime dateTo;
}
