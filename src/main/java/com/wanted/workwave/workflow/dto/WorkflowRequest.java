package com.wanted.workwave.workflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanted.workwave.workflow.domain.Workflow;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowRequest {

    @JsonProperty("team_id")
    @NotNull
    private Long teamId;

    @JsonProperty("workflow_name")
    @NotBlank
    private String workflowName;

    public Workflow toEntity(int position) {
        return Workflow.builder()
                .teamId(teamId)
                .name(workflowName)
                .position(position)
                .build();
    }
}
