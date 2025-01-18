package com.axe.stockOnHand.DTO;

import com.axe.product.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class PickedStockPostDTO {
//    ProductType productType;
    String coilNumber;
//    BigDecimal length;
    Product product;
}
