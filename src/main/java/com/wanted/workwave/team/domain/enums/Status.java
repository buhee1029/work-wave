package com.wanted.workwave.team.domain.enums;

import lombok.Getter;

@Getter
public enum Status {
    PENDING("대기"),
    APPROVED("승인");

    private final String value;

    Status(String value) {
        this.value = value;
    }
}
