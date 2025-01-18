package com.axe.consumablesInWarehouse.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Data
@EqualsAndHashCode(callSuper = true)
public class ConsumableInWarehouseNotFoundException extends RuntimeException {
    private final int code;
    public ConsumableInWarehouseNotFoundException(int code, String message) {
        super(message);
        this.code = code;
    }
}
