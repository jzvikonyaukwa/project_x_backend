package com.axe.common.charts;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartMultiEventData implements Serializable
{
   Double max;
   Double min;
   String name;
   String series;
   Double value;
}
