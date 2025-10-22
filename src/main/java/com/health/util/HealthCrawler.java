package com.health.util;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
public class HealthCrawler {

    public List<Map<String, String>> crawlDxyColdData() throws IOException {
        List<Map<String, String>> dataList = new ArrayList<>();
        String url = "https://www.zxwys.com/";
        // 模拟浏览器请求（避免被反爬）
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .header("Connection", "keep-alive")
                .referrer("https://www.google.com/")
                .ignoreHttpErrors(true)
                .timeout(10000)
                .get();

        // 获得不同问题页的Url，放到集合
        Elements questionUrl = doc.select("div.item");  // 问题网址
        List<String> Url = new ArrayList<>();
        for (Element element : questionUrl) {
            Elements answers = element.getElementsByTag("a");
            String href = answers.attr("href");
            Url.add(href);
        }
//        for (String s : Url) {
//            System.out.println(s);
//        }
        //访问每一个问题页面病爬取问题和答案
        for (String href : Url) {
            Document doc1 = Jsoup.connect(href)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .header("Connection", "keep-alive")
                    .referrer("https://www.google.com/")
                    .ignoreHttpErrors(true)
                    .timeout(10000)
                    .get();
            Element question = doc1.selectFirst("div.title h1");
            String title = question != null ? question.text() : "未找到问题";
            Element answer = doc1.selectFirst("div.con.htmlcontent");
            String content = answer != null ? answer.text() : "未找到答案";
            //System.out.println(title+"\n" + content);
            //过滤无效数据（问题或答案过短）
            Map<String, String> qaMap = new HashMap<>();
            if (title.length() > 5 && content.length() > 20) {
                qaMap.put("question", title);
                qaMap.put("answer", content);
            }
            dataList.add(qaMap);
        }
        return dataList;
    }
}
