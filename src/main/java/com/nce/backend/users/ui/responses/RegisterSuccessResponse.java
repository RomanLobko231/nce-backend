package com.nce.backend.users.ui.responses;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RegisterSuccessResponse(
        UUID userId,
        String name
) {
}
