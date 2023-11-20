package com.wanted.workwave.workflow.domain;

import com.wanted.workwave.workflow.dto.WorkflowRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "workflows")
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long teamId;
    private String name;
    private int position;

    @Builder
    public Workflow(Long teamId, String name, int position) {
        this.teamId = teamId;
        this.name = name;
        this.position = position;
    }

    public void changeWorkFlow(WorkflowRequest request) {
        this.name = request.getWorkflowName();
    }
}
