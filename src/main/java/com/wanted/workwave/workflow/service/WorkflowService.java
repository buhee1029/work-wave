package com.wanted.workwave.workflow.service;

import com.wanted.workwave.team.domain.repository.TeamMemberRepository;
import com.wanted.workwave.workflow.domain.entity.Workflow;
import com.wanted.workwave.workflow.domain.repository.WorkRepository;
import com.wanted.workwave.workflow.domain.repository.WorkflowRepository;
import com.wanted.workwave.workflow.dto.request.WorkflowRequest;
import com.wanted.workwave.workflow.dto.response.WorkResponse;
import com.wanted.workwave.workflow.dto.response.WorkflowListResponse;
import com.wanted.workwave.workflow.dto.response.WorkflowResponse;
import com.wanted.workwave.workflow.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final TeamMemberRepository teamMemberRepository;
    private final WorkRepository workRepository;
    private final WorkflowRepository workflowRepository;

    @Transactional(readOnly = true)
    public List<WorkflowListResponse> getWorkflows(Long userId, Long teamId) {
        validateLoggedInUserIsTeamMember(teamId, userId);

        return workflowRepository
                .findByTeamIdOrderByPosition(teamId).stream()
                .map(workflow -> {
                    List<WorkResponse> workResponses =
                            workRepository.findByWorkflowIdOrderByPositionAsc(workflow.getId())
                                    .stream()
                                    .map(WorkResponse::from)
                                    .collect(Collectors.toList());

                    return WorkflowListResponse.from(workflow, workResponses);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkflowResponse createWorkflow(Long userId, Long teamId, WorkflowRequest request) {
        validateLoggedInUserIsTeamMember(teamId, userId);
        int newPosition = countWorkFlow(teamId) + 1;

        return WorkflowResponse.from(workflowRepository.save(request.toEntity(teamId, newPosition)));
    }

    @Transactional
    public WorkflowResponse updateWorkflow(Long userId, Long teamId, Long workflowId, WorkflowRequest request) {
        Workflow workflow = findWorkflow(workflowId);

        validateMatchTeamWorkflow(workflow.getTeamId(), teamId);
        validateLoggedInUserIsTeamMember(teamId, userId);

        workflow.changeWorkFlowInfo(request);

        return WorkflowResponse.from(workflow);
    }

    @Transactional
    public WorkflowResponse moveWorkflow(Long userId, Long teamId, Long workflowId, int newPosition) {
        Workflow workflow = findWorkflow(workflowId);

        validateMatchTeamWorkflow(workflow.getTeamId(), teamId);
        validateLoggedInUserIsTeamMember(teamId, userId);

        int oldPosition = workflow.getPosition();

        // 현재 위치와 새로운 위치가 다를 경우에만 처리
        if (oldPosition != newPosition) {
            validateNewPosition(newPosition, getMaxPositionForTeam(teamId));
            moveWorkflowsWithinRange(teamId, oldPosition, newPosition);
            workflow.changeWorkFlowPosition(newPosition);
        }

        return WorkflowResponse.from(workflow);
    }

    @Transactional
    public void deleteWorkflow(Long userId, Long teamId, Long workflowId) {
        Workflow workflow = findWorkflow(workflowId);

        validateMatchTeamWorkflow(workflow.getTeamId(), teamId);
        validateLoggedInUserIsTeamMember(teamId, userId);
        validateWorkflowHasWorks(workflowId);

        moveWorkflowsWithinRange(teamId, workflow.getPosition(), getMaxPositionForTeam(teamId));
        workflowRepository.delete(workflow);
    }

    private Workflow findWorkflow(Long workflowId) {
        return workflowRepository.findById(workflowId).orElseThrow(NotFoundWorkflowException::new);
    }

    public void validateMatchTeamWorkflow(Long teamId, Long checkTeamId) {
        if (!teamId.equals(checkTeamId)) {
            throw new MismatchedTeamWorkflowException();
        }
    }

    private void validateLoggedInUserIsTeamMember(Long teamId, Long userId) {
        boolean isTeamMember = teamMemberRepository.existsByTeamIdAndUserId(teamId, userId);
        if (!isTeamMember) {
            throw new NotLoggedInUserTeamMemberException();
        }
    }

    private void validateWorkflowHasWorks(Long workflowId) {
        int count = workRepository.countByWorkflowId(workflowId);
        if (count > 0) {
            throw new WorkflowHasWorksException();
        }
    }

    private int countWorkFlow(Long teamId) {
        return workflowRepository.countByTeamId(teamId);
    }

    private int getMaxPositionForTeam(Long teamId) {
        return workflowRepository.findTopByTeamIdOrderByPositionDesc(teamId).map(Workflow::getPosition).orElse(1);
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
        if (start > end) { // 현재 위치보다 높은 위치의 열들을 한 칸씩 아래로 이동
            for (Workflow w : getWorkflowsInPositionRange(teamId, end, start - 1)) {
                w.changeWorkFlowPosition(w.getPosition() + 1);
            }
        } else { // 현재 위치보다 낮은 위치의 열들을 한 칸씩 위로 이동
            for (Workflow w : getWorkflowsInPositionRange(teamId, start + 1, end)) {
                w.changeWorkFlowPosition(w.getPosition() - 1);
            }
        }
    }

}
