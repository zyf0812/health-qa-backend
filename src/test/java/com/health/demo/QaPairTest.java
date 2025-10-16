package com.health.demo;

import com.health.entity.QaPair;
import org.junit.jupiter.api.Test;  // JUnit 5 注解（关键）
import static org.junit.jupiter.api.Assertions.*;  // 可选，用于断言

// 注意：JUnit 5 测试类不需要继承任何类
public class QaPairTest {

    // 直接用 @Test 注解，无需其他配置
    @Test
    void testLombok() {  // JUnit 5 中方法可以不是 public（推荐省略）
        QaPair qaPair = new QaPair();
        qaPair.setId(1);
        qaPair.setQuestion("测试问题");
        qaPair.setAnswer("测试答案");
        // 可选：添加断言（验证结果是否符合预期，更规范）
        assertEquals(1, qaPair.getId());  // 断言 ID 为 1
        assertTrue(qaPair.getQuestion().contains("测试"));  // 断言问题包含“测试”
        // 必须有打印语句才会有输出
        System.out.println("ID: " + qaPair.getId());
        System.out.println("问题: " + qaPair.getQuestion());
        System.out.println("答案: " + qaPair.getAnswer());
    }
}