package com.wanted.workwave.workflow.service;

import com.wanted.workwave.team.domain.repository.TeamMemberRepository;
import com.wanted.workwave.workflow.domain.Workflow;
import com.wanted.workwave.workflow.domain.WorkflowRepository;
import com.wanted.workwave.workflow.dto.WorkflowCreateRequest;
import com.wanted.workwave.workflow.dto.WorkflowResponse;
import com.wanted.workwave.workflow.dto.WorkflowUpdateRequest;
import com.wanted.workwave.workflow.exception.NotFoundWorkflowException;
import com.wanted.workwave.workflow.exception.NotTeamMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final TeamMemberRepository teamMemberRepository;
    private final WorkflowRepository workflowRepository;

    @Transactional
    public WorkflowResponse createWorkflow(Long userId, WorkflowCreateRequest request) {
        isTeamMember(request.getTeamId(), userId);
        int newPosition = countWorkFlow(request.getTeamId()) + 1;

        return WorkflowResponse.from(workflowRepository.save(request.toEntity(newPosition)));
    }

    @Transactional
    public WorkflowResponse updateWorkflow(Long userId, Long workflowId, WorkflowUpdateRequest request) {
        Workflow workflow = findWorkflow(workflowId);
        isTeamMember(workflow.getTeamId(), userId);
        workflow.changeWorkFlow(request);

        return WorkflowResponse.from(workflow);
    }

    private void isTeamMember(Long teamId, Long userId) {
        boolean isTeamMember = teamMemberRepository.existsByTeamIdAndUserId(teamId, userId);
        if (!isTeamMember) {
            throw new NotTeamMemberException();
        }
    }

    private int countWorkFlow(Long teamId) {
        return workflowRepository.countByTeamId(teamId);
    }

    private Workflow findWorkflow(Long workflowId) {
        return workflowRepository.findById(workflowId).orElseThrow(NotFoundWorkflowException::new);
    }

}
