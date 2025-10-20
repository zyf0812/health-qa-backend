package com.health.entity;

import lombok.Data;

@Data
public class Entity_relation {
    private int id;
    private int entity_id;
    private int target_id;
    private int rel_type;
    private String description;
}
