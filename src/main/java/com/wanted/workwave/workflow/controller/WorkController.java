package com.wanted.workwave.workflow.controller;

import com.wanted.workwave.common.response.ApiResponse;
import com.wanted.workwave.workflow.dto.WorkRequest;
import com.wanted.workwave.workflow.dto.WorkResponse;
import com.wanted.workwave.workflow.service.WorkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "작업(티켓)")
@RestController
@RequestMapping("/works")
@RequiredArgsConstructor
public class WorkController {

    private final WorkService workService;

    @PostMapping
    public ApiResponse<WorkResponse> createWork(
            @RequestAttribute Long userId,
            @Valid @RequestBody WorkRequest request) {
        return ApiResponse.created(workService.createWork(userId, request));
    }
}
