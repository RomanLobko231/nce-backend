package com.nce.backend.cars.exceptions;

public class OnAuctionUpdateNotAllowedException extends RuntimeException {
    public OnAuctionUpdateNotAllowedException(String message) {
        super(message);
    }
}
