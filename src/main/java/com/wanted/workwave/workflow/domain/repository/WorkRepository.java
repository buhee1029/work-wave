package com.wanted.workwave.workflow.domain.repository;

import com.wanted.workwave.workflow.domain.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<Work, Long> {

}
