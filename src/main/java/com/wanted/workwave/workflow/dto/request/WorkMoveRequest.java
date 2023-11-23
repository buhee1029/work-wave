package com.wanted.workwave.workflow.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkMoveRequest {

    @JsonProperty("new_workflow_id")
    private Long newWorkflowId;

    @JsonProperty("new_position")
    private int newPosition;
}
