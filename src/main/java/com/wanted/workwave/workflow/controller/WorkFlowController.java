package com.wanted.workwave.workflow.controller;

import com.wanted.workwave.common.response.ApiResponse;
import com.wanted.workwave.workflow.dto.WorkflowRequest;
import com.wanted.workwave.workflow.dto.WorkflowResponse;
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
            @Valid @RequestBody WorkflowRequest request) {
        return ApiResponse.created(workflowService.createWorkflow(userId, request));
    }
}
