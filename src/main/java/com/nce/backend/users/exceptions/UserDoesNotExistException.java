package com.nce.backend.users.exceptions;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(String message) {
        super(message);
    }
}
