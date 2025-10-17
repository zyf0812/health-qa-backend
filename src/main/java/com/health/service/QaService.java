package com.health.service;

import com.health.repository.QaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service  // 标识这是服务类，处理业务逻辑
public class QaService {
    // 自动注入Repository（无需new，Spring帮我们创建对象）
    @Autowired
    private QaRepository qaRepository;

    // 问答查询业务方法
    public String getAnswer(String question) {
        //测试异常
        throw new RuntimeException("测试异常");
        // 调用Repository查询
//        String answer = qaRepository.findAnswerByQuestion(question);
//        // 处理无结果场景
//        return answer == null ? "暂无相关健康知识，可尝试提问：“感冒症状”“高血压饮食”" : answer;
    }
}

