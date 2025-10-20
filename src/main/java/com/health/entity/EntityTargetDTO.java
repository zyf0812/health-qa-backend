package com.health.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityTargetDTO {
    private Integer sourceId;
    private String sourceName;
    private Integer targetId;
    private String targetName;
    private String relType;
    private String description;
}

