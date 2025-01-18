package com.axe.delivery_notes.delivery_notesDTOs.delivery_notes_inventories;

import com.axe.product_type.ProductType;
import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String code;
    private String frameName;
    private Double totalLength;
    private String frameType;
    private String productName;
    private String status;
    private Long totalQuantity;
    private ProductType productType;

}