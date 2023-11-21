package com.wanted.workwave.workflow.domain.repository;

import com.wanted.workwave.workflow.domain.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkflowRepository extends JpaRepository<Workflow, Long> {

    List<Workflow> findByTeamIdOrderByPosition(Long teamId);

    int countByTeamId(Long teamId);

    List<Workflow> findByTeamIdAndPositionBetween(Long teamId, int start, int end);

    Workflow findTopByTeamIdOrderByPositionDesc(Long teamId);

}
