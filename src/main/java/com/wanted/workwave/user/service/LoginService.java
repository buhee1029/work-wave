package com.wanted.workwave.user.service;

import com.wanted.workwave.common.jwt.JwtProvider;
import com.wanted.workwave.user.domain.User;
import com.wanted.workwave.user.domain.UserRepository;
import com.wanted.workwave.user.dto.LoginRequest;
import com.wanted.workwave.user.dto.LoginResponse;
import com.wanted.workwave.user.exception.NotFoundUsernameException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                                  .orElseThrow(NotFoundUsernameException::new);

        user.checkPasswordMatches(request.getPassword(), passwordEncoder);

        return LoginResponse.toResponse(jwtProvider.generateToken(user));
    }

}
