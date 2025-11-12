package com.health.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class EntityRelation {
    @Id
    private Integer id;
    private Integer source_id;
    private Integer target_id;
    private String relType;
    private String description;
}
