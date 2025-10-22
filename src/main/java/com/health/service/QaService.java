package com.health.service;

import com.health.repository.QaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service  // 标识这是服务类，处理业务逻辑
public class QaService {
    // 自动注入Repository（无需new，Spring帮我们创建对象）
    @Autowired
    private QaRepository qaRepository;

    // 问答查询业务方法
    public List<String> getAnswer(String question) {
        // 调用Repository查询
        List<String> answer = qaRepository.findAnswerByQuestion(question);
        // 处理无结果场景
        if (answer.isEmpty()) {
            List<String> list = new ArrayList<>();
            list.add("暂无相关健康知识，可尝试提问：“感冒症状”“高血压饮食");
            return list;
        }
        return answer;
    }
}

