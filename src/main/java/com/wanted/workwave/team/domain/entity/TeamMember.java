package com.wanted.workwave.team.domain.entity;

import com.wanted.workwave.team.domain.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private Long userId;
    private Long teamId;
    private Role role;

    public TeamMember(Long userId, Long teamId, Role role) {
        this.userId = userId;
        this.teamId = teamId;
        this.role = role;
    }
}
