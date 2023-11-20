package com.wanted.workwave.workflow.service;

import com.wanted.workwave.team.domain.repository.TeamMemberRepository;
import com.wanted.workwave.workflow.domain.Workflow;
import com.wanted.workwave.workflow.domain.WorkflowRepository;
import com.wanted.workwave.workflow.dto.WorkflowRequest;
import com.wanted.workwave.workflow.dto.WorkflowResponse;
import com.wanted.workwave.workflow.exception.MismatchedTeamWorkflowException;
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
    public WorkflowResponse createWorkflow(Long userId, Long teamId, WorkflowRequest request) {
        checkTeamMember(teamId, userId);
        int newPosition = countWorkFlow(teamId) + 1;

        return WorkflowResponse.from(workflowRepository.save(request.toEntity(teamId, newPosition)));
    }

    @Transactional
    public WorkflowResponse updateWorkflow(Long userId, Long teamId, Long workflowId, WorkflowRequest request) {
        Workflow workflow = findWorkflow(workflowId);
        checkMatchTeamWorkflow(workflow.getTeamId(), teamId);
        checkTeamMember(teamId, userId);
        workflow.changeWorkFlow(request);

        return WorkflowResponse.from(workflow);
    }

    @Transactional
    public void deleteWorkflow(Long userId, Long teamId, Long workflowId) {
        Workflow workflow = findWorkflow(workflowId);
        checkMatchTeamWorkflow(workflow.getTeamId(), teamId);
        checkTeamMember(teamId, userId);
        workflowRepository.delete(workflow);
    }

    private void checkTeamMember(Long teamId, Long userId) {
        boolean isTeamMember = teamMemberRepository.existsByTeamIdAndUserId(teamId, userId);
        if (!isTeamMember) {
            throw new NotTeamMemberException();
        }
    }

    public void checkMatchTeamWorkflow(Long teamId, Long checkTeamId) {
        if (!teamId.equals(checkTeamId)) {
            throw new MismatchedTeamWorkflowException();
        }
    }

    private int countWorkFlow(Long teamId) {
        return workflowRepository.countByTeamId(teamId);
    }

    private Workflow findWorkflow(Long workflowId) {
        return workflowRepository.findById(workflowId).orElseThrow(NotFoundWorkflowException::new);
    }

}
