package com.health.entity;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data  // Lombok注解，简化代码
public class QaPair {
    @Id
    private Integer id;          // 主键ID
    private String question;     // 问题
    private String answer;       // 答案

}
