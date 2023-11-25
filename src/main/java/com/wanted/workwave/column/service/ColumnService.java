package com.wanted.workwave.column.service;

import com.wanted.workwave.team.domain.repository.TeamMemberRepository;
import com.wanted.workwave.column.domain.entity.Column;
import com.wanted.workwave.column.domain.repository.TicketRepository;
import com.wanted.workwave.column.domain.repository.ColumnRepository;
import com.wanted.workwave.column.dto.request.ColumnRequest;
import com.wanted.workwave.column.dto.response.TicketResponse;
import com.wanted.workwave.column.dto.response.ColumnListResponse;
import com.wanted.workwave.column.dto.response.ColumnResponse;
import com.wanted.workwave.column.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColumnService {

    private final TeamMemberRepository teamMemberRepository;
    private final TicketRepository ticketRepository;
    private final ColumnRepository columnRepository;

    @Transactional(readOnly = true)
    public List<ColumnListResponse> getColumns(Long userId, Long teamId) {
        validateLoggedInUserIsTeamMember(teamId, userId);

        return columnRepository
                .findByTeamIdOrderByPosition(teamId).stream()
                .map(column -> {
                    List<TicketResponse> ticketResponse =
                            ticketRepository.findByColumnIdOrderByPositionAsc(column.getId())
                                    .stream()
                                    .map(TicketResponse::from)
                                    .collect(Collectors.toList());

                    return ColumnListResponse.from(column, ticketResponse);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ColumnResponse createColumn(Long userId, Long teamId, ColumnRequest request) {
        validateLoggedInUserIsTeamMember(teamId, userId);
        int newPosition = countColumn(teamId) + 1;

        return ColumnResponse.from(columnRepository.save(request.toEntity(teamId, newPosition)));
    }

    @Transactional
    public ColumnResponse updateColumn(Long userId, Long teamId, Long columnId, ColumnRequest request) {
        Column column = findColumn(columnId);

        validateMatchTeamColumn(column.getTeamId(), teamId);
        validateLoggedInUserIsTeamMember(teamId, userId);

        column.changeColumnInfo(request);

        return ColumnResponse.from(column);
    }

    @Transactional
    public ColumnResponse moveColumn(Long userId, Long teamId, Long columnId, int newPosition) {
        Column column = findColumn(columnId);

        validateMatchTeamColumn(column.getTeamId(), teamId);
        validateLoggedInUserIsTeamMember(teamId, userId);

        int oldPosition = column.getPosition();

        // 현재 위치와 새로운 위치가 다를 경우에만 처리
        if (oldPosition != newPosition) {
            validateNewPosition(newPosition, getMaxPositionForTeam(teamId));
            moveColumnsWithinRange(teamId, oldPosition, newPosition);
            column.changeColumnPosition(newPosition);
        }

        return ColumnResponse.from(column);
    }

    @Transactional
    public void deleteColumn(Long userId, Long teamId, Long columnId) {
        Column column = findColumn(columnId);

        validateMatchTeamColumn(column.getTeamId(), teamId);
        validateLoggedInUserIsTeamMember(teamId, userId);
        validateColumnHasTickets(columnId);

        moveColumnsWithinRange(teamId, column.getPosition(), getMaxPositionForTeam(teamId));
        columnRepository.delete(column);
    }

    private Column findColumn(Long columnId) {
        return columnRepository.findById(columnId).orElseThrow(NotFoundColumnException::new);
    }

    public void validateMatchTeamColumn(Long teamId, Long checkTeamId) {
        if (!teamId.equals(checkTeamId)) {
            throw new MismatchedTeamColumnException();
        }
    }

    private void validateLoggedInUserIsTeamMember(Long teamId, Long userId) {
        boolean isTeamMember = teamMemberRepository.existsByTeamIdAndUserId(teamId, userId);
        if (!isTeamMember) {
            throw new NotLoggedInUserTeamMemberException();
        }
    }

    private void validateColumnHasTickets(Long columnId) {
        int count = ticketRepository.countByColumnId(columnId);
        if (count > 0) {
            throw new ColumnHasTicketsException();
        }
    }

    private int countColumn(Long teamId) {
        return columnRepository.countByTeamId(teamId);
    }

    private int getMaxPositionForTeam(Long teamId) {
        return columnRepository.findTopByTeamIdOrderByPositionDesc(teamId).map(Column::getPosition).orElse(1);
    }

    private void validateNewPosition(int newPosition, int maxPosition) {
        if (newPosition < 1 || newPosition > maxPosition) {
            throw new InvalidPositionException();
        }
    }

    private List<Column> getColumnsInPositionRange(Long teamId, int start, int end) {
        return columnRepository.findByTeamIdAndPositionBetween(teamId, start, end);
    }

    private void moveColumnsWithinRange(Long teamId, int start, int end) {
        if (start > end) { // 현재 위치보다 높은 위치의 열들을 한 칸씩 아래로 이동
            for (Column w : getColumnsInPositionRange(teamId, end, start - 1)) {
                w.changeColumnPosition(w.getPosition() + 1);
            }
        } else { // 현재 위치보다 낮은 위치의 열들을 한 칸씩 위로 이동
            for (Column w : getColumnsInPositionRange(teamId, start + 1, end)) {
                w.changeColumnPosition(w.getPosition() - 1);
            }
        }
    }

}
