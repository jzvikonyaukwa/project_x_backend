package com.axe.clients.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper = true)
@Getter
//Should make this Generic for all classes
public class ClientNotFoundException extends RuntimeException {
    private final int code;
    public ClientNotFoundException(int code, String message) {
        super(message);
        this.code = code;

    }

}
