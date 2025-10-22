package com.health.service;

import com.health.entity.QaPair;
import com.health.repository.QaRepository;
import com.health.util.HealthCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DataImportService {
    @Autowired
    private HealthCrawler healthCrawler;

    @Autowired
    private QaRepository qaRepository;

    // 爬取并导入问答数据（去重）
    public String importQaData() {
        try {
            // 1. 爬虫获取数据
            List<Map<String, String>> dataList = healthCrawler.crawlDxyColdData();
            if (dataList.isEmpty()) {
                return "未爬取到数据，请检查爬虫配置！";
            }

            // 2. 去重并导入数据库
            int successCount = 0;
            for (Map<String, String> data : dataList) {
                String question = data.get("question");
                String answer = data.get("answer");

                // 去重：查询是否已有相同问题（模糊匹配，避免重复）
                List<String> existingAnswer = qaRepository.findAnswerByQuestion(question);
                if (existingAnswer == null) {
                    QaPair qaPair = new QaPair();
                    qaPair.setQuestion(question);
                    qaPair.setAnswer(answer);
                    qaRepository.save(qaPair);  // 保存到数据库
                    successCount++;
                }
            }

            return "数据导入完成！共爬取" + dataList.size() + "条，成功导入" + successCount + "条（已去重）";
        } catch (IOException e) {
            e.printStackTrace();
            return "数据导入失败：" + e.getMessage();
        }
    }
}
