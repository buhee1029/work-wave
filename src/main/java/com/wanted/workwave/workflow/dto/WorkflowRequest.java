package com.wanted.workwave.workflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanted.workwave.workflow.domain.Workflow;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowRequest {

    @JsonProperty("workflow_name")
    @NotBlank(message = "워크플로우 이름을 입력해주세요.")
    private String workflowName;

    public Workflow toEntity(Long teamId, int position) {
        return Workflow.builder()
                .teamId(teamId)
                .name(workflowName)
                .position(position)
                .build();
    }
}
