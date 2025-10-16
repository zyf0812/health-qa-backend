package com.health.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

// 1. 加载主类所有配置（包含数据库连接）
// 2. 明确告诉 Spring：QaRepository 在 com.health.repository 包下
@SpringBootTest(classes = com.health.demo.DemoApplication.class)
public class QaRepositoryTest {

    @Autowired
    private QaRepository qaRepository;

    // 必须调用 QaRepository 的测试方法
    @Test
    public void testQaRepositoryQuery() {
        // 直接调用接口方法
        String answer = qaRepository.findAnswerByQuestion("感冒");

        // 验证结果
        assertNotNull(answer, "调用 QaRepository 成功，查到“感冒”的答案");
        System.out.println("QaRepository 查询结果：" + answer);
    }
}