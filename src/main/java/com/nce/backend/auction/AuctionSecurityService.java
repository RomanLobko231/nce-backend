package com.nce.backend.auction;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface AuctionSecurityService {
    UUID getCurrentUserId();
}
