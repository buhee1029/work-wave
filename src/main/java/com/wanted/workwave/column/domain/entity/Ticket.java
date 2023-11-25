package com.wanted.workwave.column.domain.entity;

import com.wanted.workwave.column.domain.enums.Tag;
import com.wanted.workwave.column.dto.request.TicketRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long columnId;
    private Long assigneeId;
    private int position;
    private String title;

    @Enumerated(EnumType.STRING)
    private Tag tag;
    private double workload;
    private LocalDate deadline;

    @Builder
    public Ticket(
            Long columnId, Long assigneeId, int position,
            String title, Tag tag, double workload, LocalDate deadline) {
        this.columnId = columnId;
        this.assigneeId = assigneeId;
        this.position = position;
        this.title = title;
        this.tag = tag;
        this.workload = workload;
        this.deadline = deadline;
    }

    public void changeTicketInfo(TicketRequest request) {
        this.assigneeId = request.getAssigneeId();
        this.title = request.getTitle();
        this.tag = request.getTag();
        this.workload = request.getWorkload();
        this.deadline = request.getDeadline();
    }

    public void changeColumn(Long newColumnId) {
        this.columnId = newColumnId;
    }

    public void changePosition(int newPosition) {
        this.position = newPosition;
    }

}
