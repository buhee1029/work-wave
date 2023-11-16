package com.wanted.workwave.common.error;

import com.wanted.workwave.common.jwt.exception.ExpiredTokenException;
import com.wanted.workwave.common.jwt.exception.MissingRequestHeaderAuthorizationException;
import com.wanted.workwave.user.exception.DuplicateUsernameException;
import com.wanted.workwave.user.exception.MismatchedPasswordException;
import com.wanted.workwave.user.exception.NotFoundUsernameException;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    U001("U001", "중복된 계정입니다.", DuplicateUsernameException.class, HttpStatus.CONFLICT),
    U002("U002", "존재하지 않는 계정입니다.", NotFoundUsernameException.class, HttpStatus.NOT_FOUND),
    U003("U003", "비밀번호가 일치하지 않습니다.", MismatchedPasswordException.class, HttpStatus.NOT_FOUND),

    T001("T001", "헤더에 토큰이 존재하지 않습니다.", MissingRequestHeaderAuthorizationException.class, HttpStatus.UNAUTHORIZED),
    T002("T002", "만료된 토큰입니다.", ExpiredTokenException.class, HttpStatus.UNAUTHORIZED);

    private final String code;
    private final String message;
    private final Class<? extends CustomException> classType;
    private final HttpStatus httpStatus;
    private static final List<ErrorType> errorTypes = Arrays.stream(ErrorType.values()).toList();

    public static ErrorType of(Class<? extends CustomException> classType) {
        return errorTypes.stream()
                         .filter(it -> it.classType.equals(classType))
                         .findFirst()
                         .orElseThrow(RuntimeException::new);
    }
}
