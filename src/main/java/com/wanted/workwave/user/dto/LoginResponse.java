package com.wanted.workwave.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    String accessToken;

    @Builder
    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static LoginResponse toResponse(String token) {
        return LoginResponse.builder()
                            .accessToken(token)
                            .build();
    }

}
