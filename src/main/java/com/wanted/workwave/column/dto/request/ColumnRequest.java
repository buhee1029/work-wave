package com.wanted.workwave.column.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanted.workwave.column.domain.entity.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ColumnRequest {

    @JsonProperty("column_name")
    @NotBlank(message = "칸반보드 열 이름을 입력해주세요.")
    private String columnName;

    public Column toEntity(Long teamId, int position) {
        return Column.builder()
                .teamId(teamId)
                .name(columnName)
                .position(position)
                .build();
    }
}
