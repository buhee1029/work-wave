package com.wanted.workwave.team.domain.entity;

import com.wanted.workwave.team.domain.enums.Role;
import jakarta.persistence.Entity;
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
@Table(name = "team_members")
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long teamId;
    private Long userId;
    private Role role;

    @Builder
    public TeamMember(Long userId, Long teamId, Role role) {
        this.userId = userId;
        this.teamId = teamId;
        this.role = role;
    }

    public static TeamMember createTeamMember(Long teamId, Long userId, Role role) {
        return TeamMember.builder()
                         .teamId(teamId)
                         .userId(userId)
                         .role(role)
                         .build();
    }
}
