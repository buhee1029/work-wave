package com.wanted.workwave.workflow.domain.repository;

import com.wanted.workwave.workflow.domain.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkRepository extends JpaRepository<Work, Long> {
    List<Work> findByWorkflowId(Long workflowId);

    int countByWorkflowId(Long workflowId);

    Work findTopByWorkflowIdOrderByPositionDesc(Long workflowId);

    List<Work> findByWorkflowIdAndPositionBetween(Long workflowId, int start, int end);
}
