package com.axe.utilities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Data
@EqualsAndHashCode(callSuper = true)
public class InvoiceNotFoundException extends RuntimeException{
    private final int code;
    public InvoiceNotFoundException(int code, String message) {
        super(message);
        this.code = code;
    }

}
