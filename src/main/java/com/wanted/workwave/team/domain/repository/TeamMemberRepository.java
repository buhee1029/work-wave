package com.wanted.workwave.team.domain.repository;

import com.wanted.workwave.team.domain.entity.TeamMember;
import com.wanted.workwave.team.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    boolean existsByTeamIdAndUserIdAndRole(Long teamId, Long userId, Role role);
}
