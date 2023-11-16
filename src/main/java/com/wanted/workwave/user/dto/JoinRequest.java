package com.wanted.workwave.user.dto;

import com.wanted.workwave.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {

    @NotBlank(message = "계정을 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    public User toEntity() {
        return User.builder()
                   .username(username)
                   .password(password)
                   .build();
    }

}