package com.clara.discographyservice.application.port.in;

public class DeserializerDiscogsResponseException extends RuntimeException {
    public DeserializerDiscogsResponseException(String message, Throwable cause) {
        super(message, cause);
    }

}
