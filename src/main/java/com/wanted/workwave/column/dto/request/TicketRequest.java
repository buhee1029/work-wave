package com.wanted.workwave.column.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanted.workwave.column.domain.entity.Ticket;
import com.wanted.workwave.column.domain.enums.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest {

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private String title;
    private Tag tag;
    private double workload;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate deadline;

    public Ticket toEntity(Long columnId, int position) {
        return Ticket.builder()
                .columnId(columnId)
                .assigneeId(assigneeId)
                .title(title)
                .tag(tag)
                .workload(workload)
                .deadline(deadline)
                .position(position)
                .build();
    }
}
