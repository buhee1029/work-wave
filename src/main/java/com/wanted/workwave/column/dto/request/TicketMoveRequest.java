package com.wanted.workwave.column.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TicketMoveRequest {

    @JsonProperty("new_column_id")
    private Long newColumnId;

    @JsonProperty("new_position")
    private int newPosition;
}
