package com.health.controller;

import com.health.service.QaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QaController {
    @Autowired
    private QaService qaService;

    // 问答查询接口：POST请求，参数question
    @PostMapping("/api/qa/query")
    public String queryAnswer(@RequestParam String question) {
        // 调用Service处理业务，返回结果
        return qaService.getAnswer(question);
    }
}
