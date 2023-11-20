package com.wanted.workwave.workflow.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowRepository extends JpaRepository<Workflow, Long> {

    int countByTeamId(Long teamId);
}
