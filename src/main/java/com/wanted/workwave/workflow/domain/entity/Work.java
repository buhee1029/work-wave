package com.wanted.workwave.workflow.domain.entity;

import com.wanted.workwave.workflow.domain.enums.Tag;
import com.wanted.workwave.workflow.dto.request.WorkRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "works")
public class Work {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long workflowId;
    private Long assigneeId;
    private int position;
    private String title;

    @Enumerated(EnumType.STRING)
    private Tag tag;
    private double workload;
    private LocalDateTime deadline;

    @Builder
    public Work(
            Long workflowId, Long assigneeId, int position,
            String title, Tag tag, double workload, LocalDateTime deadline) {
        this.workflowId = workflowId;
        this.assigneeId = assigneeId;
        this.position = position;
        this.title = title;
        this.tag = tag;
        this.workload = workload;
        this.deadline = deadline;
    }

    public void changeWorkInfo(WorkRequest request) {
        this.assigneeId = request.getAssigneeId();
        this.title = request.getTitle();
        this.tag = request.getTag();
        this.workload = request.getWorkload();
        this.deadline = request.getDeadline();
    }
}
