package com.clara.discographyservice.application.port.out;

public class DiscogsException extends RuntimeException {
    public DiscogsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DiscogsException(String message) {
        super(message);
    }
}
