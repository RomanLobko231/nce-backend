package com.nce.backend.auction.exceptions;

public class AuctionIllegalStateException extends RuntimeException {
    public AuctionIllegalStateException(String message) {
        super(message);
    }
}
