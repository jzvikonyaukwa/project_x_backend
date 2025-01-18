package com.axe.common.charts;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartMultiData implements Serializable
{
   String name;
   List<ChartSingleData> series;
}
