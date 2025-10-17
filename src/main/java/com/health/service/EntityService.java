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

    //根据类型查询实体方法
    public List<Entity> getEntity(String type){
        List<Entity> list = entityRepository.findByType(type);
        return list == null?new ArrayList<Entity>():list;
    }
}
