package com.health.controller;

import com.health.common.Result;
import com.health.entity.Entity;
import com.health.service.DetailService;
import com.health.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DetailController {
    @Autowired
    private DetailService detailService;

    /**
     * 根据ID查询实体详情
     * @param id 实体ID
     * @return 返回查询到的实体对象
     */
    @GetMapping("/api/entity/detail")
    public Result<Entity> IdFindEntity(@RequestParam int id) {
        return Result.success(detailService.getEntity(id));
    }

}
