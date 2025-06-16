package com.nce.backend.logging.application.port;

import java.util.UUID;

public interface Authenticator {
    String getCurrentUserName();
    UUID getCurrentUserId();
}
