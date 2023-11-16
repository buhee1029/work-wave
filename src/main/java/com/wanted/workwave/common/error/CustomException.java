package com.wanted.workwave.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorType errorType = ErrorType.of(this.getClass());
}