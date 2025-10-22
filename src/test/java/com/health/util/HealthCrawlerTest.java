package com.health.util;


import com.health.util.HealthCrawler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = com.health.demo.DemoApplication.class)
public class HealthCrawlerTest {

    @Autowired
    private HealthCrawler healthCrawler;

    @Test
    void testJsoup() throws IOException {
        List<Map<String, String>> list = healthCrawler.crawlDxyColdData();
        for (Map<String, String> map : list) {
            map.forEach((k, v) -> {
//                System.out.println(k + "\t" + v);
            });
        }
    }
}
