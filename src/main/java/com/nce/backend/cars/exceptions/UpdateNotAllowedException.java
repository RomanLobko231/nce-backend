package com.nce.backend.cars.exceptions;

public class UpdateNotAllowedException extends RuntimeException {
    public UpdateNotAllowedException(String message) {
        super(message);
    }
}
