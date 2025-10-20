package com.health.service;

import com.health.entity.EntityTargetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<EntityTargetDTO> getDTOBySource_Id(Integer id) {
        String sql = """
            SELECT 
                er.source_id AS sourceId,
                e1.name AS sourceName,
                er.target_id AS targetId,
                e2.name AS targetName,
                er.relType AS relType,
                er.description AS description
            FROM entity_relation er
            JOIN `entity` e1 ON er.source_id = e1.id
            JOIN `entity` e2 ON er.target_id = e2.id
            WHERE er.source_id = ?
        """;

        return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            EntityTargetDTO dto = new EntityTargetDTO();
            dto.setSourceId(rs.getInt("sourceId"));
            dto.setSourceName(rs.getString("sourceName"));
            dto.setTargetId(rs.getInt("targetId"));
            dto.setTargetName(rs.getString("targetName"));
            dto.setRelType(rs.getString("relType"));
            dto.setDescription(rs.getString("description"));
            return dto;
        });
    }
}
