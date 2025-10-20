package com.health.repository;

import com.health.entity.EntityRelation;
import com.health.entity.EntityTargetDTO;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationRepository extends CrudRepository<EntityRelation, Integer> {

    @Query("""
        SELECT 
            er.source_id   AS sourceId,
            e1.name        AS sourceName,
            er.target_id   AS targetId,
            e2.name        AS targetName,
            er.relType     AS relType,
            er.description AS description
        FROM entity_relation er
        JOIN `entity` e1 ON er.source_id = e1.id
        JOIN `entity` e2 ON er.target_id = e2.id
        WHERE er.source_id = :id
    """)
    List<EntityTargetDTO> findRelationsBySourceId(@Param("id") Integer id);
}
