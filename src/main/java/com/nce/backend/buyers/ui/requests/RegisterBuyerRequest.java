package com.nce.backend.buyers.ui.requests;

public record RegisterBuyerRequest(
        String name,

        String phoneNumber,

        String email,

        Integer orgNumber,

        String address
) {
}
