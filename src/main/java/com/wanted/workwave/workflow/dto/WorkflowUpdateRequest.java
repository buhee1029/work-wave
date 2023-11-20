package com.wanted.workwave.workflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowUpdateRequest {

    @JsonProperty("workflow_name")
    @NotBlank
    private String workflowName;

}
