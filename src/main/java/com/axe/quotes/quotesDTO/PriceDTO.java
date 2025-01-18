package com.axe.quotes.quotesDTO;

import com.axe.colors.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class PriceDTO {
    Long id;
    BigDecimal stdPrice;
    BigDecimal tradesMenPrice;
    Color color;
    ProductTypeDTO productType;

}