package com.health.repository;

import com.health.entity.Entity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DetailRepository extends CrudRepository<Entity, Integer> {
    //根据type查询实体并返回list
    Entity findById(int id);
}
