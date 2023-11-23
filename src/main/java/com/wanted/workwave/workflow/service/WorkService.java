package com.wanted.workwave.workflow.service;

import com.wanted.workwave.team.domain.repository.TeamMemberRepository;
import com.wanted.workwave.workflow.domain.entity.Work;
import com.wanted.workwave.workflow.domain.entity.Workflow;
import com.wanted.workwave.workflow.domain.repository.WorkRepository;
import com.wanted.workwave.workflow.domain.repository.WorkflowRepository;
import com.wanted.workwave.workflow.dto.request.WorkMoveRequest;
import com.wanted.workwave.workflow.dto.request.WorkRequest;
import com.wanted.workwave.workflow.dto.response.WorkResponse;
import com.wanted.workwave.workflow.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public WorkResponse moveWork(Long userId, Long workflowId, Long workId, WorkMoveRequest request) {
        // 원래 워크플로우와 새 워크플로우 가져오기
        Workflow originWorkflow = findWorkflow(workflowId);
        Workflow newWorkflow = findWorkflow(request.getNewWorkflowId());

        // 사용자가 팀의 멤버인지 확인
        validateLoggedInUserIsTeamMember(originWorkflow.getTeamId(), userId);
        validateLoggedInUserIsTeamMember(newWorkflow.getTeamId(), userId);

        // 이동할 작업과 위치 정보 가져오기
        Work work = findWork(workId);
        int oldPosition = work.getPosition();
        int newPosition = request.getNewPosition();

        // 다른 워크플로우로 이동한다면
        if (!originWorkflow.equals(newWorkflow)) {
            Long newWorkflowId = request.getNewWorkflowId();
            int maxPositionInNew = getMaxPositionForWorkflow(newWorkflowId) + 1;
            int differ = maxPositionInNew - newPosition;

            // 새 위치 확인 및 작업 이동
            validateNewPosition(newPosition, maxPositionInNew);
            moveWorksWithinRange(newWorkflowId, newPosition + differ, maxPositionInNew - differ);

            // 작업의 워크플로우 및 위치 변경
            work.changeWorkflow(newWorkflowId);
            work.changePosition(newPosition);

            // 이전 워크플로우에서 작업 위치 변경
            int maxPositionInOrigin = getMaxPositionForWorkflow(workflowId);
            moveWorksWithinRange(workflowId, oldPosition, maxPositionInOrigin);
        } else {
            // 기존 워크플로우에서 이동한다면
            if (oldPosition != newPosition) {
                validateNewPosition(newPosition, getMaxPositionForWorkflow(workflowId));
                moveWorksWithinRange(workflowId, oldPosition, newPosition);
                work.changePosition(newPosition);
            }
        }

        return WorkResponse.from(work);
    }

    @Transactional
    public void deleteWork(Long userId, Long workflowId, Long workId) {
        Workflow workflow = findWorkflow(workflowId);

        validateLoggedInUserIsTeamMember(workflow.getTeamId(), userId);

        Work work = findWork(workId);
        moveWorksWithinRange(workflowId, work.getPosition(), getMaxPositionForWorkflow(workflowId));
        workRepository.delete(work);
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

    private void validateNewPosition(int newPosition, int maxPosition) {
        if (newPosition < 1 || newPosition > maxPosition) {
            throw new InvalidPositionException();
        }
    }

    private int getMaxPositionForWorkflow(Long workflowId) {
        return workRepository.findTopByWorkflowIdOrderByPositionDesc(workflowId).map(Work::getPosition).orElse(1);
    }

    private List<Work> getWorksInPositionRange(Long workflowId, int start, int end) {
        return workRepository.findByWorkflowIdAndPositionBetween(workflowId, start, end);
    }

    private void moveWorksWithinRange(Long workflowId, int start, int end) {
        if (start > end) {
            for (Work w : getWorksInPositionRange(workflowId, end, start - 1)) {
                w.changePosition(w.getPosition() + 1);
            }
        } else {
            for (Work w : getWorksInPositionRange(workflowId, start + 1, end)) {
                w.changePosition(w.getPosition() - 1);
            }
        }
    }
}
