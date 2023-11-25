package com.wanted.workwave.column.controller;

import com.wanted.workwave.common.response.ApiResponse;
import com.wanted.workwave.column.dto.request.TicketMoveRequest;
import com.wanted.workwave.column.dto.request.TicketRequest;
import com.wanted.workwave.column.dto.response.TicketResponse;
import com.wanted.workwave.column.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "티켓(작업)")
@RestController
@RequestMapping("/api/columns/{columnId}/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ApiResponse<TicketResponse> createTicket(
            @RequestAttribute Long userId,
            @PathVariable Long columnId,
            @Valid @RequestBody TicketRequest request) {
        return ApiResponse.created(ticketService.createTicket(userId, columnId, request));
    }

    @PutMapping("/{ticketId}")
    public ApiResponse<TicketResponse> updateTicket(
            @RequestAttribute Long userId,
            @PathVariable Long columnId,
            @PathVariable Long ticketId,
            @Valid @RequestBody TicketRequest request) {
        return ApiResponse.ok(ticketService.updateTicket(userId, columnId, ticketId, request));
    }

    @PatchMapping("/{ticketId}/move")
    public ApiResponse<TicketResponse> moveTicket(
            @RequestAttribute Long userId,
            @PathVariable Long columnId,
            @PathVariable Long ticketId,
            @Valid @RequestBody TicketMoveRequest request) {
        return ApiResponse.ok(ticketService.moveTicket(userId, columnId, ticketId, request));
    }

    @DeleteMapping("/{ticketId}")
    public ApiResponse<Void> deleteTicket(
            @RequestAttribute Long userId,
            @PathVariable Long columnId,
            @PathVariable Long ticketId) {
        ticketService.deleteTicket(userId, columnId, ticketId);
        return ApiResponse.noContent();
    }
}
