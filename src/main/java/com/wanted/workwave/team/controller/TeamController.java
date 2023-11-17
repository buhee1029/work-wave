package com.wanted.workwave.team.controller;

import com.wanted.workwave.common.response.ApiResponse;
import com.wanted.workwave.team.dto.request.TeamCreateRequest;
import com.wanted.workwave.team.dto.request.TeamInviteRequest;
import com.wanted.workwave.team.dto.response.TeamInfoResponse;
import com.wanted.workwave.team.dto.response.TeamInviteResponse;
import com.wanted.workwave.team.service.TeamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "팀")
@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ApiResponse<TeamInfoResponse> createTeam(
            @RequestAttribute Long userId,
            @Valid @RequestBody TeamCreateRequest request) {
        return ApiResponse.created(teamService.createTeam(userId, request));
    }

    @PostMapping("/{teamId}/invite")
    public ApiResponse<TeamInviteResponse> inviteTeamMember(
            @RequestAttribute Long userId,
            @PathVariable Long teamId,
            @Valid @RequestBody TeamInviteRequest request) {
        return ApiResponse.created(teamService.inviteTeamMember(userId, teamId, request));
    }

}
