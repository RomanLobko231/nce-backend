package com.nce.backend.auction.application.port;

import java.util.UUID;

public interface Authenticator {
    UUID getCurrentUserId();
}
