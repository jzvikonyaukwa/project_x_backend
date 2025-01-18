package com.axe.quotes.quotesDTO;

import com.axe.gauges.Gauge;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductTypeDTO {
    Long id;
    String type;
    Gauge gauge;
}
