package com.health.controller;

import com.health.common.Result;
import com.health.entity.Entity;
import com.health.service.EntityService;
import com.health.service.QaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EntityController {
    @Autowired
    private EntityService entityService;

    @PostMapping("/api/entity/list")
    public Result<List<Entity>> typeFindEntity(@RequestBody String type) {
        // 参数校验（非空判断）
        if (type == null || type.trim().isEmpty()) {
            List<Entity> list = entityService.getAllEntity();
            return Result.success(list);
        }
        List<Entity> list = entityService.getEntity(type);
        return Result.success(list);
    }
}
