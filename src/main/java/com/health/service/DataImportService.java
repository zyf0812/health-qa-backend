package com.health.service;

import com.health.entity.Entity;
import com.health.entity.QaPair;
import com.health.repository.EntityRepository;
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
    private EntityRepository entityRepository;


    public String importQaData() {
        List<Map<String, String>> data = healthCrawler.crawlDxyColdData();
        List<Entity> entities = new ArrayList<>();
        for (Map<String, String> map : data) {
            Entity entity = new Entity();
            entity.setName(map.get("疾病名"));
            entity.setType("疾病");
            entity.setDesc(map.get("简介"));
            entities.add(entity);
        }
        entityRepository.saveAll(entities);
        return "数据导入成功";
    }
}