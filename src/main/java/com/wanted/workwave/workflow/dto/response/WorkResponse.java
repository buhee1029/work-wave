package com.wanted.workwave.workflow.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanted.workwave.workflow.domain.entity.Work;
import com.wanted.workwave.workflow.domain.enums.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class WorkResponse {

    @JsonProperty("work_id")
    private Long workId;

    @JsonProperty("workflow_id")
    private Long workflowId;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private int position;
    private String title;
    private Tag tag;
    private double workload;
    private LocalDate deadline;

    public static WorkResponse from(Work work) {
        return WorkResponse.builder()
                .workId(work.getId())
                .workflowId(work.getWorkflowId())
                .assigneeId(work.getAssigneeId())
                .position(work.getPosition())
                .title(work.getTitle())
                .tag(work.getTag())
                .workload(work.getWorkload())
                .deadline(work.getDeadline())
                .build();
    }
}
