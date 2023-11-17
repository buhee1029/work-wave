package com.wanted.workwave.team.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamInviteRequest {

    @NotBlank(message = "초대할 계정을 입력해주세요.")
    private String username;

}
