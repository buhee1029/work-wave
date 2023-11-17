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
import com.wanted.workwave.team.exception.AlreadyApprovedInviteException;
import com.wanted.workwave.team.exception.InvalidInviteAccessException;
import com.wanted.workwave.team.exception.NotFoundTeamInviteException;
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
        teamMemberRepository.save(TeamMember.createTeamMember(teamId, userId, Role.LEADER));

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

    @Transactional
    public void approveInvite(Long userId, Long inviteId) {
        TeamInvite invite = findTeamInvite(inviteId);

        validateUserAccess(userId, invite);
        validateInviteNotAlreadyApproved(invite);

        invite.approveInvite();

        teamMemberRepository.save(TeamMember.createTeamMember(invite.getTeamId(), userId, Role.MEMBER));
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

    private TeamInvite findTeamInvite(Long inviteId) {
        return teamInviteRepository.findById(inviteId)
                                   .orElseThrow(NotFoundTeamInviteException::new);
    }

    private void validateUserAccess(Long userId, TeamInvite invite) {
        if (!invite.getUserId().equals(userId)) {
            throw new InvalidInviteAccessException();
        }
    }

    private void validateInviteNotAlreadyApproved(TeamInvite invite) {
        if (invite.getStatus().equals(Status.APPROVED)) {
            throw new AlreadyApprovedInviteException();
        }
    }
}
