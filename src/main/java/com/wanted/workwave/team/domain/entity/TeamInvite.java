package com.wanted.workwave.team.domain.entity;

import com.wanted.workwave.team.domain.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "team_invites")
public class TeamInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long teamId;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public TeamInvite(Long teamId, Long userId, Status status) {
        this.teamId = teamId;
        this.userId = userId;
        this.status = status;
    }

    public static TeamInvite createTeamInvite(Long teamId, Long userId, Status status) {
        return TeamInvite.builder()
                         .teamId(teamId)
                         .userId(userId)
                         .status(status)
                         .build();
    }
}
