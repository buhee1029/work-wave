package com.wanted.workwave.team.domain.repository;

import com.wanted.workwave.team.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

}
