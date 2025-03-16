package com.nce.backend.users.ui.responses;

import com.nce.backend.users.domain.entities.User;
import org.springframework.stereotype.Service;

@Service
public class UserResponseMapper {

    public RegisterSuccessResponse toRegisterSuccessResponse(User registeredUser) {
        return RegisterSuccessResponse
                .builder()
                .userId(registeredUser.getId())
                .name(registeredUser.getName())
                .build();
    }
}
