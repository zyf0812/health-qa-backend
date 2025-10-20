package com.health.controller;

import com.health.common.Result;
import com.health.entity.EntityTargetDTO;
import com.health.repository.RelationRepository;
import com.health.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RelationController {
    @Autowired
    private RelationService relationService;

    @GetMapping("/api/entity/relation")
    public Result<List<EntityTargetDTO>>  getTargetDTO(@RequestParam Integer id) {
        return Result.success(relationService.getDTOBySource_Id(id));
    }
}
