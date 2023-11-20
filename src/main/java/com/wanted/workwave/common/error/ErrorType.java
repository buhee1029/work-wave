package com.wanted.workwave.common.error;

import com.wanted.workwave.common.jwt.exception.ExpiredTokenException;
import com.wanted.workwave.common.jwt.exception.MissingRequestHeaderAuthorizationException;
import com.wanted.workwave.team.exception.AlreadyApprovedInviteException;
import com.wanted.workwave.team.exception.InvalidInviteAccessException;
import com.wanted.workwave.team.exception.NotFoundTeamInviteException;
import com.wanted.workwave.team.exception.NotTeamLeaderException;
import com.wanted.workwave.user.exception.DuplicateUsernameException;
import com.wanted.workwave.user.exception.MismatchedPasswordException;
import com.wanted.workwave.user.exception.NotFoundUsernameException;
import com.wanted.workwave.workflow.exception.NotFoundWorkflowException;
import com.wanted.workwave.workflow.exception.NotTeamMemberException;
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
    T002("T002", "만료된 토큰입니다.", ExpiredTokenException.class, HttpStatus.UNAUTHORIZED),

    I001("I001", "팀장만 팀월을 초대할 수 있습니다.", NotTeamLeaderException.class, HttpStatus.NOT_FOUND),
    I002("I002", "존재하지 않는 초대입니다.", NotFoundTeamInviteException.class, HttpStatus.NOT_FOUND),
    I003("I003", "초대를 승낙할 수 없습니다.", InvalidInviteAccessException.class, HttpStatus.FORBIDDEN),
    I004("I004", "이미 승인된 초대입니다.", AlreadyApprovedInviteException.class, HttpStatus.CONFLICT),

    W001("W001", "팀 멤버에게만 접근 권한이 있습니다.",NotTeamMemberException.class, HttpStatus.FORBIDDEN),
    W002("W002", "존재하지 않는 워크플로우 입니다.", NotFoundWorkflowException.class, HttpStatus.NOT_FOUND);

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
