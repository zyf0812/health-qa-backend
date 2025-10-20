package com.health.controller;

import com.health.common.Result;
import com.health.entity.Entity;
import com.health.service.DetailService;
import com.health.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DetailController {
    @Autowired
    private DetailService detailService;

    @GetMapping("/api/entity/detail")
    public Result<Entity> IdFindEntity(@RequestParam String id) {
        return Result.success(detailService.getEntity(id));
    }
}
