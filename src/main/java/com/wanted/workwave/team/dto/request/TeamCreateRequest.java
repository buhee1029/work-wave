package com.wanted.workwave.team.dto.request;

import com.wanted.workwave.team.domain.entity.Team;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamCreateRequest {

    @NotBlank(message = "팀명을 입력해주세요.")
    private String teamName;

    public Team toEntity() {
        return Team.builder()
                   .teamName(teamName)
                   .build();
    }
}
