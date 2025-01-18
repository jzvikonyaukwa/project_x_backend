package com.axe.grvs.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper = true)
@Getter
public class NoProductsFoundException extends RuntimeException {
    private final int code;
    public NoProductsFoundException(int code,String message) {
        super(message);
        this.code = code;

    }

}
