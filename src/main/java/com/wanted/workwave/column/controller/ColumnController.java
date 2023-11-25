package com.wanted.workwave.column.controller;

import com.wanted.workwave.common.response.ApiResponse;
import com.wanted.workwave.column.dto.request.ColumnRequest;
import com.wanted.workwave.column.dto.response.ColumnListResponse;
import com.wanted.workwave.column.dto.response.ColumnResponse;
import com.wanted.workwave.column.service.ColumnService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "칸반보드 열")
@RestController
@RequestMapping("/api/teams/{teamId}/columns")
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService columnService;

    @GetMapping
    public ApiResponse<List<ColumnListResponse>> getColumns(
            @RequestAttribute Long userId,
            @PathVariable Long teamId) {
        return ApiResponse.ok(columnService.getColumns(userId, teamId));
    }

    @PostMapping
    public ApiResponse<ColumnResponse> createColumn(
            @RequestAttribute Long userId,
            @PathVariable Long teamId,
            @Valid @RequestBody ColumnRequest request) {
        return ApiResponse.created(columnService.createColumn(userId, teamId, request));
    }

    @PutMapping("/{columnId}")
    public ApiResponse<ColumnResponse> updateColumn(
            @RequestAttribute Long userId,
            @PathVariable Long teamId,
            @PathVariable Long columnId,
            @Valid @RequestBody ColumnRequest request) {
        return ApiResponse.ok(columnService.updateColumn(userId, teamId, columnId, request));
    }

    @PatchMapping("/{columnId}/move/{newPosition}")
    public ApiResponse<ColumnResponse> moveColumn(
            @RequestAttribute Long userId,
            @PathVariable Long teamId,
            @PathVariable Long columnId,
            @PathVariable int newPosition) {
        return ApiResponse.ok(columnService.moveColumn(userId, teamId, columnId, newPosition));
    }

    @DeleteMapping("/{columnId}")
    public ApiResponse<Void> deleteColumn(
            @RequestAttribute Long userId,
            @PathVariable Long teamId,
            @PathVariable Long columnId) {
        columnService.deleteColumn(userId, teamId, columnId);
        return ApiResponse.noContent();
    }
}
