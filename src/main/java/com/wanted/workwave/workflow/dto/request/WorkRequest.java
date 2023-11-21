package com.wanted.workwave.workflow.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanted.workwave.workflow.domain.entity.Work;
import com.wanted.workwave.workflow.domain.enums.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkRequest {

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private String title;
    private Tag tag;
    private double workload;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime deadline;

    public Work toEntity(Long workflowId, int position) {
        return Work.builder()
                .workflowId(workflowId)
                .assigneeId(assigneeId)
                .title(title)
                .tag(tag)
                .workload(workload)
                .deadline(deadline)
                .position(position)
                .build();
    }
}
