package com.axe.steelCoils.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Data
@EqualsAndHashCode(callSuper = true)
public class SteelCoilNotFoundException extends RuntimeException {
    public SteelCoilNotFoundException( String message) {
        super(message);

    }
}
