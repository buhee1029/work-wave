package com.wanted.workwave.column.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanted.workwave.column.domain.entity.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ColumnListResponse {
    @JsonProperty("column_id")
    private Long columnId;

    @JsonProperty("team_id")
    private Long teamId;

    private String name;
    private int position;
    private List<TicketResponse> tickets;

    public static ColumnListResponse from(Column column, List<TicketResponse> tickets) {
        return ColumnListResponse.builder()
                .columnId(column.getId())
                .teamId(column.getTeamId())
                .name(column.getName())
                .position(column.getPosition())
                .tickets(tickets)
                .build();
    }
}
