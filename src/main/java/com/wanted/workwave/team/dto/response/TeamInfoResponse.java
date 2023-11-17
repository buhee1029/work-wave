package com.wanted.workwave.team.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanted.workwave.team.domain.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TeamInfoResponse {

    @JsonProperty("team_id")
    private Long teamId;

    @JsonProperty("team_name")
    private String teamName;

    public static TeamInfoResponse from(Team team) {
        return TeamInfoResponse.builder()
                               .teamId(team.getId())
                               .teamName(team.getTeamName())
                               .build();

    }
}
