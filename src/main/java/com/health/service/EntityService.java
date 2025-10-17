package com.health.service;

import com.health.entity.Entity;
import com.health.repository.EntityRepository;
import com.health.repository.QaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntityService {

    @Autowired
    private EntityRepository entityRepository;

    // 根据类型查询实体（type为空时查询所有）
    public List<Entity> getEntity(String type) {
        List<Entity> list;
        if (type == null || type.trim().isEmpty()) {
            // 若type为空（或仅空格），查询所有实体
            list = entityRepository.findAll();
        } else {
            // 若type不为空，按类型查询
            list = entityRepository.findByType(type);
        }
        // 确保返回空列表而非null
        return list == null ? new ArrayList<>() : list;
    }
}