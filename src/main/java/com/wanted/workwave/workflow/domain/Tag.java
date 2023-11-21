package com.wanted.workwave.workflow.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.wanted.workwave.workflow.exception.InvalidTagException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Tag {
    FRONTEND,
    BACKEND,
    DESIGN,
    QA,
    PM,
    DOCUMENT;

    Tag() {}

    @JsonCreator
    public static Tag of(String input) {
        return Arrays.stream(Tag.values())
                .filter(tag -> tag.name().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(InvalidTagException::new);
    }

}
