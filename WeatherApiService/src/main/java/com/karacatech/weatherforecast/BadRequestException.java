package com.karacatech.weatherforecast;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
