package com.wanted.workwave.workflow.controller;

import com.wanted.workwave.common.response.ApiResponse;
import com.wanted.workwave.workflow.dto.WorkflowRequest;
import com.wanted.workwave.workflow.dto.WorkflowResponse;
import com.wanted.workwave.workflow.service.WorkflowService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "워크플로우(칸반보드)")
@RestController
@RequestMapping("teams/{teamId}/workflows")
@RequiredArgsConstructor
public class WorkFlowController {

    private final WorkflowService workflowService;

    @GetMapping
    public ApiResponse<List<WorkflowResponse>> getWorkflows(
            @RequestAttribute Long userId,
            @PathVariable Long teamId) {
        return ApiResponse.ok(workflowService.getWorkflows(userId, teamId));
    }

    @PostMapping
    public ApiResponse<WorkflowResponse> createWorkflow(
            @RequestAttribute Long userId,
            @PathVariable Long teamId,
            @Valid @RequestBody WorkflowRequest request) {
        return ApiResponse.created(workflowService.createWorkflow(userId, teamId, request));
    }

    @PutMapping("/{workflowId}")
    public ApiResponse<WorkflowResponse> updateWorkflow(
            @RequestAttribute Long userId,
            @PathVariable Long teamId,
            @PathVariable Long workflowId,
            @Valid @RequestBody WorkflowRequest request) {
        return ApiResponse.created(workflowService.updateWorkflow(userId, teamId, workflowId, request));
    }

    @DeleteMapping("/{workflowId}")
    public ApiResponse<Void> deleteWorkflow(
            @RequestAttribute Long userId,
            @PathVariable Long teamId,
            @PathVariable Long workflowId) {
        workflowService.deleteWorkflow(userId, teamId, workflowId);
        return ApiResponse.noContent();
    }
}
