package com.axe.utilities;

public final class ClientVisibleException extends RuntimeException {
    public ClientVisibleException(String message) {
        super(message);
    }

    public ClientVisibleException(String message, Throwable cause) {
        super(message, cause);
    }
}