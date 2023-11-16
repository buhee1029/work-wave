package com.wanted.workwave.common.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
        ErrorType errorType = exception.getErrorType();

        log.error("[error] type: {}, status: {}, message: {}",
                  errorType.getClassType().getSimpleName(),
                  errorType.getHttpStatus(),
                  errorType.getMessage());

        return ResponseEntity
                .status(errorType.getHttpStatus())
                .body(ErrorResponse.of(errorType));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        log.error("[error] type: {}, status: {}, message: {}",
                  exception.getClass().getSimpleName(),
                  exception.getStatusCode(),
                  exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(String.valueOf(HttpStatus.BAD_REQUEST.value()), getMessage(exception)));
    }

    private String getMessage(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
    }

}