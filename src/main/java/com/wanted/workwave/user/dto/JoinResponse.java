package com.wanted.workwave.user.dto;

import lombok.Getter;

@Getter
public class JoinResponse {
    private Long id;
    private String username;

    public JoinResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }

}
