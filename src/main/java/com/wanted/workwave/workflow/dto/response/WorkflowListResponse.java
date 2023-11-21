package com.wanted.workwave.workflow.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanted.workwave.workflow.domain.entity.Workflow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class WorkflowListResponse {
    @JsonProperty("workflow_id")
    private Long workflowId;

    @JsonProperty("team_id")
    private Long teamId;

    private String name;
    private int position;
    private List<WorkResponse> works;

    public static WorkflowListResponse from(Workflow workflow, List<WorkResponse> works) {
        return WorkflowListResponse.builder()
                .workflowId(workflow.getId())
                .teamId(workflow.getTeamId())
                .name(workflow.getName())
                .position(workflow.getPosition())
                .works(works)
                .build();
    }
}
