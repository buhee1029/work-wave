package com.wanted.workwave.column.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanted.workwave.column.domain.entity.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ColumnResponse {

    @JsonProperty("column_id")
    private Long columnId;

    @JsonProperty("team_id")
    private Long teamId;

    private String name;
    private int position;

    public static ColumnResponse from(Column column) {
        return ColumnResponse.builder()
                .columnId(column.getId())
                .teamId(column.getTeamId())
                .name(column.getName())
                .position(column.getPosition())
                .build();
    }
}
