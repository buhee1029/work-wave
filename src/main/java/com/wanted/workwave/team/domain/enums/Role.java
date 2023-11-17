package com.wanted.workwave.team.domain.enums;

import lombok.Getter;

@Getter
public enum Role {
    LEADER("팀장"),
    MEMBER("팀원");

    private final String value;

    Role(String value) {
        this.value = value;
    }

}
