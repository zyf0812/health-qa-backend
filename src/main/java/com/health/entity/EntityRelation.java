package com.health.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("entity_relation")
public class EntityRelation {
    @Id
    private Integer id;
    private Integer source_id;
    private Integer target_id;
    private String relType;
    private String description;
}
