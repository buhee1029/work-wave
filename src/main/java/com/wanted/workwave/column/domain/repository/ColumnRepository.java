package com.wanted.workwave.column.domain.repository;

import com.wanted.workwave.column.domain.entity.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ColumnRepository extends JpaRepository<Column, Long> {

    List<Column> findByTeamIdOrderByPosition(Long teamId);

    int countByTeamId(Long teamId);

    List<Column> findByTeamIdAndPositionBetween(Long teamId, int start, int end);

    Optional<Column> findTopByTeamIdOrderByPositionDesc(Long teamId);

}
