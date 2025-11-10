package com.health.service;

import com.health.entity.QaPair;
import com.health.repository.QaRepository;
import com.health.util.HealthCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DataImportService {
    @Autowired
    private HealthCrawler healthCrawler;

    @Autowired
    private QaRepository qaRepository;

    // 爬取并导入问答数据（去重）
    public String importQaData() {
        // 正则表达式：匹配"你好"后面跟任意一个字符（包括中文标点等）
        String regex = "你|您好.";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);

        try {
            List<Map<String, String>> dataList = healthCrawler.crawlDxyColdData();
            if (dataList.isEmpty()) {
                return "未爬取到数据，请检查爬虫配置！";
            }

            int successCount = 0;
            List<QaPair> list = new ArrayList<>();
            for (Map<String, String> data : dataList) {
                String question = data.get("question");
                String answer = data.get("answer");
                if (answer == null || answer.isEmpty()) {
                    continue;
                }

                // 过滤特定前缀的答案
                if (answer.startsWith("病情分析") || answer.startsWith("病情评估")) {
                    continue;
                }

                // 去重逻辑
                List<String> existingAnswer = qaRepository.findAnswerByQuestion(question);
                if (existingAnswer == null || existingAnswer.isEmpty()) {
                    QaPair qaPair = new QaPair();
                    qaPair.setQuestion(question);

                    //删除所有"你好+一个字符"
                    Matcher matcher = pattern.matcher(answer);
                    String processedAnswer = matcher.replaceAll("");  // 替换为""，即删除匹配内容

                    //去除多余空格并trim
                    processedAnswer = processedAnswer.replaceAll("\\s+", " ").trim();
                    qaPair.setAnswer(processedAnswer);

                    list.add(qaPair);
                    successCount++;
                }
            }
            //批量存入数据库
            qaRepository.saveAll(list);
            return "数据导入完成！共爬取" + dataList.size() + "条，成功导入" + successCount + "条（已去重）";
        } catch (IOException e) {
            e.printStackTrace();
            return "数据导入失败：" + e.getMessage();
        }
    }
}