package com.wanted.workwave.workflow.service;

import com.wanted.workwave.team.domain.repository.TeamMemberRepository;
import com.wanted.workwave.workflow.domain.Workflow;
import com.wanted.workwave.workflow.domain.WorkflowRepository;
import com.wanted.workwave.workflow.dto.WorkflowRequest;
import com.wanted.workwave.workflow.dto.WorkflowResponse;
import com.wanted.workwave.workflow.exception.InvalidPositionException;
import com.wanted.workwave.workflow.exception.MismatchedTeamWorkflowException;
import com.wanted.workwave.workflow.exception.NotFoundWorkflowException;
import com.wanted.workwave.workflow.exception.NotTeamMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final TeamMemberRepository teamMemberRepository;
    private final WorkflowRepository workflowRepository;

    @Transactional(readOnly = true)
    public List<WorkflowResponse> getWorkflows(Long userId, Long teamId) {
        checkTeamMember(teamId, userId);

        return workflowRepository.findByTeamIdOrderByPosition(teamId)
                .stream()
                .map(WorkflowResponse::from)
                .toList();
    }

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
        workflow.changeWorkFlowInfo(request);

        return WorkflowResponse.from(workflow);
    }

    @Transactional
    public void moveWorkflow(Long userId, Long teamId, Long workflowId, int newPosition) {
        Workflow workflow = findWorkflow(workflowId);
        checkMatchTeamWorkflow(workflow.getTeamId(), teamId);
        checkTeamMember(teamId, userId);

        int oldPosition = workflow.getPosition();

        // 현재 위치와 새로운 위치가 다를 경우에만 처리
        if (oldPosition != newPosition) {
            validateNewPosition(newPosition, getMaxPositionForTeam(teamId));
            moveWorkflowsWithinRange(teamId, oldPosition, newPosition);
            workflow.changeWorkFlowPosition(newPosition);
        }
    }

    @Transactional
    public void deleteWorkflow(Long userId, Long teamId, Long workflowId) {
        Workflow workflow = findWorkflow(workflowId);
        checkMatchTeamWorkflow(workflow.getTeamId(), teamId);
        checkTeamMember(teamId, userId);
        workflowRepository.delete(workflow);
    }

    private Workflow findWorkflow(Long workflowId) {
        return workflowRepository.findById(workflowId).orElseThrow(NotFoundWorkflowException::new);
    }

    public void checkMatchTeamWorkflow(Long teamId, Long checkTeamId) {
        if (!teamId.equals(checkTeamId)) {
            throw new MismatchedTeamWorkflowException();
        }
    }

    private void checkTeamMember(Long teamId, Long userId) {
        boolean isTeamMember = teamMemberRepository.existsByTeamIdAndUserId(teamId, userId);
        if (!isTeamMember) {
            throw new NotTeamMemberException();
        }
    }

    private int countWorkFlow(Long teamId) {
        return workflowRepository.countByTeamId(teamId);
    }

    private int getMaxPositionForTeam(Long teamId) {
        return workflowRepository.findTopByTeamIdOrderByPositionDesc(teamId).getPosition();

    }

    private void validateNewPosition(int newPosition, int maxPosition) {
        if (newPosition < 1 || newPosition > maxPosition) {
            throw new InvalidPositionException();
        }
    }

    private List<Workflow> getWorkflowsInPositionRange(Long teamId, int start, int end) {
        return workflowRepository.findByTeamIdAndPositionBetween(teamId, start, end);
    }

    private void moveWorkflowsWithinRange(Long teamId, int start, int end) {
        if (start > end) { // 현재 위치보다 높은 위치의 작업들을 한 칸씩 아래로 이동
            for (Workflow w : getWorkflowsInPositionRange(teamId, end, start - 1)) {
                w.changeWorkFlowPosition(w.getPosition() + 1);
            }
        } else { // 현재 위치보다 낮은 위치의 작업들을 한 칸씩 위로 이동
            for (Workflow w : getWorkflowsInPositionRange(teamId, start + 1, end)) {
                w.changeWorkFlowPosition(w.getPosition() - 1);
            }
        }
    }

}
