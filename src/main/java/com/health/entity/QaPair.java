package com.health.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data  // Lombok注解，简化代码
public class QaPair {
    @Id
    private Integer id;          // 主键ID
    private String question;     // 问题
    private String answer;       // 答案
}
