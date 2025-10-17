package com.health.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Entity {
    @Id
    private String id;      //主键id
    private String name;        //实体名称
    private String type;        //实体类型
    private String description;         //实体简介
}
