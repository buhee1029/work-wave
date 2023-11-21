package com.wanted.workwave.workflow.service;

import com.wanted.workwave.team.domain.repository.TeamMemberRepository;
import com.wanted.workwave.workflow.domain.entity.Work;
import com.wanted.workwave.workflow.domain.entity.Workflow;
import com.wanted.workwave.workflow.domain.repository.WorkRepository;
import com.wanted.workwave.workflow.domain.repository.WorkflowRepository;
import com.wanted.workwave.workflow.dto.WorkRequest;
import com.wanted.workwave.workflow.dto.WorkResponse;
import com.wanted.workwave.workflow.exception.AssigneeNotTeamMemberException;
import com.wanted.workwave.workflow.exception.NotFoundWorkException;
import com.wanted.workwave.workflow.exception.NotFoundWorkflowException;
import com.wanted.workwave.workflow.exception.NotLoggedInUserTeamMemberException;
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
    public WorkResponse createWork(Long userId, Long workflowId, WorkRequest request) {
        Workflow workflow = findWorkflow(workflowId);

        validateLoggedInUserIsTeamMember(workflow.getTeamId(), userId);
        validateAssigneeIsTeamMember(workflow.getTeamId(), request.getAssigneeId());

        int newPosition = countWork(workflowId) + 1;

        return WorkResponse.from(workRepository.save(request.toEntity(workflowId, newPosition)));
    }

    @Transactional
    public WorkResponse updateWork(Long userId, Long workflowId, Long workId, WorkRequest request) {
        Workflow workflow = findWorkflow(workflowId);

        validateLoggedInUserIsTeamMember(workflow.getTeamId(), userId);
        validateAssigneeIsTeamMember(workflow.getTeamId(), request.getAssigneeId());

        Work work = findWork(workId);
        work.changeWorkInfo(request);

        return WorkResponse.from(work);
    }

    @Transactional
    public void deleteWork(Long userId, Long workflowId, Long workId) {
        Workflow workflow = findWorkflow(workflowId);

        validateLoggedInUserIsTeamMember(workflow.getTeamId(), userId);

        workRepository.delete(findWork(workId));
    }

    private Workflow findWorkflow(Long workflowId) {
        return workflowRepository.findById(workflowId).orElseThrow(NotFoundWorkflowException::new);
    }

    private Work findWork(Long workId) {
        return workRepository.findById(workId).orElseThrow(NotFoundWorkException::new);
    }

    private void validateLoggedInUserIsTeamMember(Long teamId, Long userId) {
        boolean isTeamMember = teamMemberRepository.existsByTeamIdAndUserId(teamId, userId);
        if (!isTeamMember) {
            throw new NotLoggedInUserTeamMemberException();
        }
    }

    private void validateAssigneeIsTeamMember(Long teamId, Long assigneeId) {
        boolean isTeamMember = teamMemberRepository.existsByTeamIdAndUserId(teamId, assigneeId);
        if (!isTeamMember) {
            throw new AssigneeNotTeamMemberException();
        }
    }

    private int countWork(Long workflowId) {
        return workRepository.countByWorkflowId(workflowId);
    }
}
