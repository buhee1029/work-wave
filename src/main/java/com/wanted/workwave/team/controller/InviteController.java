package com.wanted.workwave.team.controller;

import com.wanted.workwave.common.response.ApiResponse;
import com.wanted.workwave.team.service.TeamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "초대 수락")
@RestController
@RequestMapping("/api/invites")
@RequiredArgsConstructor
public class InviteController {

    private final TeamService teamService;

    @PostMapping("/{inviteId}/accept")
    public ApiResponse<Void>approveInvite(
            @RequestAttribute Long userId,
            @PathVariable Long inviteId) {
        teamService.approveInvite(userId, inviteId);
        return ApiResponse.noContent();
    }

}
