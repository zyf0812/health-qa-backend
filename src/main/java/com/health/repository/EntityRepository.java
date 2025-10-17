package com.health.repository;

import com.health.entity.Entity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface EntityRepository extends CrudRepository<Entity, Integer> {
    /**
     * 根据实体类型筛选实体列表
     * Spring Data会自动根据方法名生成对应的SQL查询
     * 等效于: SELECT * FROM entity WHERE type = ?
     *
     * @param type 实体类型（如"疾病"、"症状"、"食材"）
     * @return 符合该类型的实体列表
     */
    List<Entity> findByType(String type);

    //查询所有实体（继承 CrudRepository 的 findAll() 方法默认返回 Iterable，这里封装为 List）
    @Override
    @NonNull
    List<Entity> findAll();

}
