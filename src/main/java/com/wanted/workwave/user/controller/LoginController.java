package com.wanted.workwave.user.controller;

import com.wanted.workwave.common.response.ApiResponse;
import com.wanted.workwave.user.dto.LoginRequest;
import com.wanted.workwave.user.dto.LoginResponse;
import com.wanted.workwave.user.service.LoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인")
@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.created(loginService.login(request));
    }

}
