package com.wanted.workwave.workflow.controller;

import com.wanted.workwave.common.response.ApiResponse;
import com.wanted.workwave.workflow.dto.WorkflowCreateRequest;
import com.wanted.workwave.workflow.dto.WorkflowResponse;
import com.wanted.workwave.workflow.dto.WorkflowUpdateRequest;
import com.wanted.workwave.workflow.service.WorkflowService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "워크플로우(칸반보드)")
@RestController
@RequestMapping("/workflows")
@RequiredArgsConstructor
public class WorkFlowController {

    private final WorkflowService workflowService;

    @PostMapping
    public ApiResponse<WorkflowResponse> createWorkflow(
            @RequestAttribute Long userId,
            @Valid @RequestBody WorkflowCreateRequest request) {
        return ApiResponse.created(workflowService.createWorkflow(userId, request));
    }

    @PutMapping("/{workflow_id}")
    public ApiResponse<WorkflowResponse> updateWorkflow(
            @RequestAttribute Long userId,
            @PathVariable("workflow_id") Long workflowId,
            @Valid @RequestBody WorkflowUpdateRequest request) {
        return ApiResponse.created(workflowService.updateWorkflow(userId, workflowId, request));
    }

    @DeleteMapping("/{workflow_id}")
    public ApiResponse<Void> deleteWorkflow(
            @RequestAttribute Long userId,
            @PathVariable("workflow_id") Long workflowId) {
        workflowService.deleteWorkflow(userId, workflowId);
        return ApiResponse.noContent();
    }
}
