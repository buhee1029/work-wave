package com.wanted.workwave.column.domain.entity;

import com.wanted.workwave.column.dto.request.ColumnRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "columns")
public class Column {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long teamId;
    private String name;
    private int position;

    @Builder
    public Column(Long teamId, String name, int position) {
        this.teamId = teamId;
        this.name = name;
        this.position = position;
    }

    public void changeColumnInfo(ColumnRequest request) {
        this.name = request.getColumnName();
    }

    public void changeColumnPosition(int newPosition) {
        this.position = newPosition;
    }
}
