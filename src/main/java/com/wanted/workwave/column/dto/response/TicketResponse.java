package com.wanted.workwave.column.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanted.workwave.column.domain.entity.Ticket;
import com.wanted.workwave.column.domain.enums.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class TicketResponse {

    @JsonProperty("ticket_id")
    private Long ticketId;

    @JsonProperty("column_id")
    private Long columnId;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private int position;
    private String title;
    private Tag tag;
    private double workload;
    private LocalDate deadline;

    public static TicketResponse from(Ticket ticket) {
        return TicketResponse.builder()
                .ticketId(ticket.getId())
                .columnId(ticket.getColumnId())
                .assigneeId(ticket.getAssigneeId())
                .position(ticket.getPosition())
                .title(ticket.getTitle())
                .tag(ticket.getTag())
                .workload(ticket.getWorkload())
                .deadline(ticket.getDeadline())
                .build();
    }
}
