package com.axe.common.charts;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartSingleData implements Serializable
{
   String name;
   Double value;
   Double min;
   Double max;
}
