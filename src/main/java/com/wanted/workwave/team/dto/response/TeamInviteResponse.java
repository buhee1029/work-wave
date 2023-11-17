package com.wanted.workwave.team.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanted.workwave.team.domain.entity.TeamInvite;
import com.wanted.workwave.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TeamInviteResponse {

    @JsonProperty("invite_id")
    private Long inviteId;

    @JsonProperty("invited_user")
    private InvitedUser invitedUser;

    public static TeamInviteResponse from(TeamInvite invite, User user) {
        return TeamInviteResponse.builder()
                                 .inviteId(invite.getId())
                                 .invitedUser(InvitedUser.from(user))
                                 .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class InvitedUser {

        @JsonProperty("user_id")
        private Long userId;
        private String username;

        public static InvitedUser from(User user) {
            return InvitedUser.builder()
                              .userId(user.getId())
                              .username(user.getUsername())
                              .build();
        }
    }
}
