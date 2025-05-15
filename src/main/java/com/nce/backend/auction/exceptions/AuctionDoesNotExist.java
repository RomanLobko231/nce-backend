package com.nce.backend.auction.exceptions;

public class AuctionDoesNotExist extends RuntimeException {
    public AuctionDoesNotExist(String message) {
        super(message);
    }
}
