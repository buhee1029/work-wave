package com.wanted.workwave.team.service;

import com.wanted.workwave.team.domain.entity.Team;
import com.wanted.workwave.team.domain.entity.TeamMember;
import com.wanted.workwave.team.domain.enums.Role;
import com.wanted.workwave.team.domain.repository.TeamMemberRepository;
import com.wanted.workwave.team.domain.repository.TeamRepository;
import com.wanted.workwave.team.dto.request.TeamCreateRequest;
import com.wanted.workwave.team.dto.response.TeamInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    public TeamInfoResponse createTeam(Long userId, TeamCreateRequest request) {
        Team team = teamRepository.save(request.toEntity());
        Long teamId = team.getId();
        teamMemberRepository.save(TeamMember.createTeamMember(userId, teamId, Role.LEADER));
        return TeamInfoResponse.from(team);
    }
}
