package com.axe.stockOnHand.DTO;

import lombok.Data;

@Data
public class ProductPickedDTO {
    Long productId;
    Long aggrProdId;
    Long stockOnHandId;
}
