package com.axe.product.productDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManufacturedResponse {

    private boolean success;
    private String message;
    private int numberOfProductsProcessed;
    private long processingTimeMillis;

    public ManufacturedResponse(boolean success, String message, int numberOfProductsProcessed, long processingTimeMillis) {
        this.success = success;
        this.message = message;
        this.numberOfProductsProcessed = numberOfProductsProcessed;
        this.processingTimeMillis = processingTimeMillis;
    }
}
