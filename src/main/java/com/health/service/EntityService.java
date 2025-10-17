package com.health.service;

import com.health.entity.Entity;
import com.health.repository.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntityService {

    @Autowired
    private EntityRepository entityRepository;

    //列表为空时，查询所有实体并返回
    public List<Entity> getAllEntity() {
        List<Entity> list;
        list = entityRepository.findAll();
        return list;
    }

    public List<Entity> getEntity(String type ) {
        List<Entity> list;
        list = entityRepository.findByType(type);
        return list;
    }
}