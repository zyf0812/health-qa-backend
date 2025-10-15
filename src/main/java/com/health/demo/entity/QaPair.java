package com.health.demo.entity;
import lombok.Data;

@Data  // Lombok注解，简化代码
public class QaPair {
    private Integer id;          // 主键ID
    private String question;     // 问题
    private String answer;       // 答案

}
