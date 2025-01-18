package com.axe.common.agGrid.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimestampColumnFilter extends ColumnFilter implements Serializable
{
   private String type;
   //   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
   private Timestamp dateFrom;
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
   private Timestamp dateTo;
}
