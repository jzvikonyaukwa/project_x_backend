package com.axe.purchaseOrders.DTOs;

import com.axe.grvs.grvsDTO.SteelCoilPostDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductsOnOrderDTO {

    private SteelCoilPostDTO steelSpecification;
    private Integer qty;
}
