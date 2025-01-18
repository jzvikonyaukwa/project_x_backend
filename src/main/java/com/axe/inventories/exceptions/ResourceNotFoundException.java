package com.axe.inventories.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceNotFoundException extends RuntimeException {
    private final int code;
    public ResourceNotFoundException(int code, String message) {
        super(message);
        this.code = code;
    }
}
