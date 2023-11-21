package com.wanted.workwave.workflow.service;

import com.wanted.workwave.team.domain.repository.TeamMemberRepository;
import com.wanted.workwave.workflow.domain.entity.Workflow;
import com.wanted.workwave.workflow.domain.repository.WorkRepository;
import com.wanted.workwave.workflow.domain.repository.WorkflowRepository;
import com.wanted.workwave.workflow.dto.WorkRequest;
import com.wanted.workwave.workflow.dto.WorkResponse;
import com.wanted.workwave.workflow.exception.NotFoundWorkflowException;
import com.wanted.workwave.workflow.exception.NotTeamMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkService {

    private final TeamMemberRepository teamMemberRepository;
    private final WorkRepository workRepository;
    private final WorkflowRepository workflowRepository;

    @Transactional
    public WorkResponse createWork(Long userId, WorkRequest request) {
        Long workflowId = request.getWorkflowId();
        Workflow workflow = findWorkflow(workflowId);

        Long teamId = workflow.getTeamId();
        checkTeamMember(teamId, userId);
        checkTeamMember(teamId, request.getAssigneeId());

        int newPosition = countWork(workflowId) + 1;

        return WorkResponse.from(workRepository.save(request.toEntity(newPosition)));
    }

    private Workflow findWorkflow(Long workflowId) {
        return workflowRepository.findById(workflowId).orElseThrow(NotFoundWorkflowException::new);
    }

    private void checkTeamMember(Long teamId, Long userId) {
        boolean isTeamMember = teamMemberRepository.existsByTeamIdAndUserId(teamId, userId);
        if (!isTeamMember) {
            throw new NotTeamMemberException();
        }
    }

    private int countWork(Long workflowId) {
        return workRepository.countByWorkflowId(workflowId);
    }
}
