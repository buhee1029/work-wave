package com.wanted.workwave.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wanted.workwave.common.jwt.exception.ExpiredTokenException;
import com.wanted.workwave.user.domain.User;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private static final String CLAIM_NAME = "id";

    public String generateToken(User user) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                  .withClaim(CLAIM_NAME, user.getId())
                  .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                  .sign(Algorithm.HMAC256(JwtProperties.SECRET));
    }

    public boolean validateToken(String token) {
        try {
            decodedToken(token);
            return true;
        } catch (TokenExpiredException exception) {
            throw new ExpiredTokenException();
        }
    }

    public Long getUserId(String token) {
        return decodedToken(token).getClaim(CLAIM_NAME)
                                  .asLong();
    }

    private DecodedJWT decodedToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JwtProperties.SECRET))
                                  .build();
        return verifier.verify(token);
    }

}
