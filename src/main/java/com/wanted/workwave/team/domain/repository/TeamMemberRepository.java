package com.wanted.workwave.team.domain.repository;

import com.wanted.workwave.team.domain.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

}
