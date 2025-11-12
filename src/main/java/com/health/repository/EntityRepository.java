package com.health.repository;

import com.health.entity.Entity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntityRepository extends CrudRepository<Entity, Integer> {
    //根据type查询实体并返回list
    List<Entity> findByType(String type);

    //查询所有实体（继承 CrudRepository 的 findAll() 方法默认返回 Iterable，这里封装为 List）
    @Override
    @NonNull
    List<Entity> findAll();

    Optional<Entity> findByNameAndType(String name, String type);
}
