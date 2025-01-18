package com.axe.utilities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SteelCoilNumberExists extends RuntimeException {
    private final int code;
    public SteelCoilNumberExists(int code,String message) {
        super(message);
        this.code = code;
    }
}

