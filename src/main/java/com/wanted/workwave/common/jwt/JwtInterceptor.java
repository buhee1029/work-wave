package com.wanted.workwave.common.jwt;

import static com.wanted.workwave.common.jwt.JwtProperties.TOKEN_PREFIX;

import com.wanted.workwave.common.jwt.exception.MissingRequestHeaderAuthorizationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader(JwtProperties.HEADER_STRING);

        if (authorization == null || !authorization.startsWith(TOKEN_PREFIX)) {
            throw new MissingRequestHeaderAuthorizationException();
        }

        String token = authorization.replaceAll(TOKEN_PREFIX, "");

        if(jwtProvider.validateToken(token)) {
            Long userId = jwtProvider.getUserId(token);
            request.setAttribute("userId", userId);

            return true;
        }

        return false;
    }
}
