package com.health.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HealthCrawler {
    // 爬取丁香医生健康科普数据（以“感冒”为例）
    public List<Map<String, String>> crawlDxyColdData() throws IOException {
        List<Map<String, String>> dataList = new ArrayList<>();
        // 丁香医生感冒科普页面（静态页面，易爬取）
        String url = "https://dxy.com/health/article/541467377";
        // 模拟浏览器请求（避免被反爬）
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .timeout(10000)  // 超时时间10秒
                .get();

        // 解析“问题-答案”对（根据页面HTML结构调整选择器，用浏览器F12查看）
        Elements questionElements = doc.select(".article-section h3");  // 问题元素
        Elements answerElements = doc.select(".article-section .article-content");  // 答案元素

        for (int i = 0; i < Math.min(questionElements.size(), answerElements.size()); i++) {
            String question = questionElements.get(i).text().replace("？", "");
            String answer = answerElements.get(i).text().trim();
            // 过滤无效数据（问题或答案过短）
            if (question.length() > 5 && answer.length() > 20) {
                Map<String, String> qaMap = new HashMap<>();
                qaMap.put("question", question);
                qaMap.put("answer", answer);
                dataList.add(qaMap);
            }
        }
        return dataList;
    }
}
