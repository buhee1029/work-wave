package com.wanted.workwave.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private int status;
    private T data;

    private ApiResponse() {
    }

    private ApiResponse(int status) {
        this.status = status;
    }

    private ApiResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), data);
    }

    public static <T> ApiResponse<T> noContent() {
        return new ApiResponse<>(HttpStatus.NO_CONTENT.value());
    }

}