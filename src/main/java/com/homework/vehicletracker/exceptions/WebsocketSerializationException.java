package com.homework.vehicletracker.exceptions;

public class WebsocketSerializationException extends RuntimeException {
    public WebsocketSerializationException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
