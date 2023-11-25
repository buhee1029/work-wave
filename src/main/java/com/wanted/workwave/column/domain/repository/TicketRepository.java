package com.wanted.workwave.column.domain.repository;

import com.wanted.workwave.column.domain.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByColumnIdOrderByPositionAsc(Long columnId);

    int countByColumnId(Long columnId);

    Optional<Ticket> findTopByColumnIdOrderByPositionDesc(Long columnId);

    List<Ticket> findByColumnIdAndPositionBetween(Long columnId, int start, int end);
}
