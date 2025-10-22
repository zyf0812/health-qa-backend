package com.health.controller;

import com.health.common.Result;
import com.health.service.DataImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataImportController {
    @Autowired
    private DataImportService dataImportService;

    // 数据导入接口（仅用于开发环境，生产环境需加权限控制）
    @PostMapping("/api/data/import-qa")
    public Result<String> importQaData() {
        String result = dataImportService.importQaData();
        return Result.success(result);
    }
}
