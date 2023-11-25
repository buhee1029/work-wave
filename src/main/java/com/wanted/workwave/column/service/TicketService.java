package com.wanted.workwave.column.service;

import com.wanted.workwave.team.domain.repository.TeamMemberRepository;
import com.wanted.workwave.column.domain.entity.Ticket;
import com.wanted.workwave.column.domain.entity.Column;
import com.wanted.workwave.column.domain.repository.TicketRepository;
import com.wanted.workwave.column.domain.repository.ColumnRepository;
import com.wanted.workwave.column.dto.request.TicketMoveRequest;
import com.wanted.workwave.column.dto.request.TicketRequest;
import com.wanted.workwave.column.dto.response.TicketResponse;
import com.wanted.workwave.column.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TeamMemberRepository teamMemberRepository;
    private final TicketRepository ticketRepository;
    private final ColumnRepository columnRepository;

    @Transactional
    public TicketResponse createTicket(Long userId, Long columnId, TicketRequest request) {
        Column workflow = findColumn(columnId);

        validateLoggedInUserIsTeamMember(workflow.getTeamId(), userId);
        validateAssigneeIsTeamMember(workflow.getTeamId(), request.getAssigneeId());

        int newPosition = countTicket(columnId) + 1;

        return TicketResponse.from(ticketRepository.save(request.toEntity(columnId, newPosition)));
    }

    @Transactional
    public TicketResponse updateTicket(Long userId, Long columnId, Long ticketId, TicketRequest request) {
        Column column = findColumn(columnId);

        validateLoggedInUserIsTeamMember(column.getTeamId(), userId);
        validateAssigneeIsTeamMember(column.getTeamId(), request.getAssigneeId());

        Ticket ticket = findTicket(ticketId);
        ticket.changeTicketInfo(request);

        return TicketResponse.from(ticket);
    }

    @Transactional
    public TicketResponse moveTicket(Long userId, Long columnId, Long ticketId, TicketMoveRequest request) {
        // 원래 워크플로우와 새 워크플로우 가져오기
        Column originColumn = findColumn(columnId);
        Column newColumn = findColumn(request.getNewColumnId());

        // 사용자가 팀의 멤버인지 확인
        validateLoggedInUserIsTeamMember(originColumn.getTeamId(), userId);
        validateLoggedInUserIsTeamMember(newColumn.getTeamId(), userId);

        // 이동할 작업과 위치 정보 가져오기
        Ticket ticket = findTicket(ticketId);
        int oldPosition = ticket.getPosition();
        int newPosition = request.getNewPosition();

        // 다른 워크플로우로 이동한다면
        if (!originColumn.equals(newColumn)) {
            Long newColumnId = request.getNewColumnId();
            int maxPositionInNew = getMaxPositionForColumn(newColumnId) + 1;
            int differ = maxPositionInNew - newPosition;

            // 새 위치 확인 및 작업 이동
            validateNewPosition(newPosition, maxPositionInNew);
            moveTicketsWithinRange(newColumnId, newPosition + differ, maxPositionInNew - differ);

            // 작업의 워크플로우 및 위치 변경
            ticket.changeColumn(newColumnId);
            ticket.changePosition(newPosition);

            // 이전 워크플로우에서 작업 위치 변경
            int maxPositionInOrigin = getMaxPositionForColumn(columnId);
            moveTicketsWithinRange(columnId, oldPosition, maxPositionInOrigin);
        } else {
            // 기존 워크플로우에서 이동한다면
            if (oldPosition != newPosition) {
                validateNewPosition(newPosition, getMaxPositionForColumn(columnId));
                moveTicketsWithinRange(columnId, oldPosition, newPosition);
                ticket.changePosition(newPosition);
            }
        }

        return TicketResponse.from(ticket);
    }

    @Transactional
    public void deleteTicket(Long userId, Long columnId, Long ticketId) {
        Column column = findColumn(columnId);
        validateLoggedInUserIsTeamMember(column.getTeamId(), userId);

        Ticket ticket = findTicket(ticketId);
        moveTicketsWithinRange(columnId, ticket.getPosition(), getMaxPositionForColumn(columnId));
        ticketRepository.delete(ticket);
    }

    private Column findColumn(Long columnId) {
        return columnRepository.findById(columnId).orElseThrow(NotFoundColumnException::new);
    }

    private Ticket findTicket(Long ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow(NotFoundTicketException::new);
    }

    private void validateLoggedInUserIsTeamMember(Long teamId, Long userId) {
        boolean isTeamMember = teamMemberRepository.existsByTeamIdAndUserId(teamId, userId);
        if (!isTeamMember) {
            throw new NotLoggedInUserTeamMemberException();
        }
    }

    private void validateAssigneeIsTeamMember(Long teamId, Long assigneeId) {
        boolean isTeamMember = teamMemberRepository.existsByTeamIdAndUserId(teamId, assigneeId);
        if (!isTeamMember) {
            throw new AssigneeNotTeamMemberException();
        }
    }

    private int countTicket(Long columnId) {
        return ticketRepository.countByColumnId(columnId);
    }

    private void validateNewPosition(int newPosition, int maxPosition) {
        if (newPosition < 1 || newPosition > maxPosition) {
            throw new InvalidPositionException();
        }
    }

    private int getMaxPositionForColumn(Long columnId) {
        return ticketRepository.findTopByColumnIdOrderByPositionDesc(columnId).map(Ticket::getPosition).orElse(1);
    }

    private List<Ticket> getTicketsInPositionRange(Long columnId, int start, int end) {
        return ticketRepository.findByColumnIdAndPositionBetween(columnId, start, end);
    }

    private void moveTicketsWithinRange(Long columnId, int start, int end) {
        if (start > end) {
            for (Ticket w : getTicketsInPositionRange(columnId, end, start - 1)) {
                w.changePosition(w.getPosition() + 1);
            }
        } else {
            for (Ticket w : getTicketsInPositionRange(columnId, start + 1, end)) {
                w.changePosition(w.getPosition() - 1);
            }
        }
    }
}
