package com.axe.consumablesOnGrv.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Data
@EqualsAndHashCode(callSuper = true)
public class ConsumableOnGrvNotFoundException extends RuntimeException {
    private final int code;
    public ConsumableOnGrvNotFoundException(int code, String message) {
        super(message);
        this.code = code;
    }
}
