package com.health.service;

import com.health.entity.QaPair;
import com.health.repository.QaRepository;
import com.health.util.AiApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class QaService {
    @Autowired
    private QaRepository qaRepository;

    @Autowired
    private AiApiClient aiApiClient;

    public List<String> getAnswer(String question) {
        // 1. 先查询本地知识库
        List<String> localAnswers = qaRepository.findAnswerByQuestion(question);
        if (!localAnswers.isEmpty()) {
            return localAnswers;
        }

        // 2. 本地无结果时调用AI接口
        String aiAnswer = aiApiClient.getAiAnswer("请回答健康问题：" + question);
        List<String> result = new ArrayList<>();
        result.add(aiAnswer);

        // 获取AI的答案后存入数据库
        QaPair qaPair = new QaPair();
        qaPair.setQuestion(question);
        qaPair.setAnswer(aiAnswer);
        qaRepository.save(qaPair);
        // 返回结果
        return result;

    }
}