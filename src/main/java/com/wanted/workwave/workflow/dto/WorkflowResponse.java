package com.wanted.workwave.workflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanted.workwave.workflow.domain.entity.Workflow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WorkflowResponse {

    @JsonProperty("workflow_id")
    private Long workflowId;

    @JsonProperty("team_id")
    private Long teamId;

    private String name;
    private int position;

    public static WorkflowResponse from(Workflow workflow) {
        return WorkflowResponse.builder()
                .workflowId(workflow.getId())
                .teamId(workflow.getTeamId())
                .name(workflow.getName())
                .position(workflow.getPosition())
                .build();
    }
}
