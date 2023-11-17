package com.wanted.workwave.team.service;

import com.wanted.workwave.team.domain.entity.Team;
import com.wanted.workwave.team.domain.entity.TeamInvite;
import com.wanted.workwave.team.domain.entity.TeamMember;
import com.wanted.workwave.team.domain.enums.Role;
import com.wanted.workwave.team.domain.enums.Status;
import com.wanted.workwave.team.domain.repository.TeamInviteRepository;
import com.wanted.workwave.team.domain.repository.TeamMemberRepository;
import com.wanted.workwave.team.domain.repository.TeamRepository;
import com.wanted.workwave.team.dto.request.TeamCreateRequest;
import com.wanted.workwave.team.dto.request.TeamInviteRequest;
import com.wanted.workwave.team.dto.response.TeamInfoResponse;
import com.wanted.workwave.team.dto.response.TeamInviteResponse;
import com.wanted.workwave.team.exception.NotTeamLeaderException;
import com.wanted.workwave.user.domain.User;
import com.wanted.workwave.user.domain.UserRepository;
import com.wanted.workwave.user.exception.NotFoundUsernameException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamInviteRepository teamInviteRepository;

    @Transactional
    public TeamInfoResponse createTeam(Long userId, TeamCreateRequest request) {
        Team team = teamRepository.save(request.toEntity());
        Long teamId = team.getId();
        teamMemberRepository.save(TeamMember.createTeamMember(userId, teamId, Role.LEADER));
        return TeamInfoResponse.from(team);
    }

    @Transactional
    public TeamInviteResponse inviteTeamMember(Long userId, Long teamId, TeamInviteRequest request) {
        isTeamLeader(userId, teamId);
        User user = findUser(request.getUsername());
        TeamInvite invite = teamInviteRepository.save(
                TeamInvite.createTeamInvite(teamId, user.getId(), Status.PENDING)
        );

        return TeamInviteResponse.from(invite, user);
    }

    private void isTeamLeader(Long teamId, Long userId) {
        boolean isTeamLeader = teamMemberRepository.existsByTeamIdAndUserIdAndRole(teamId, userId, Role.LEADER);
        if (!isTeamLeader) {
            throw new NotTeamLeaderException();
        }
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                             .orElseThrow(NotFoundUsernameException::new);
    }
}
