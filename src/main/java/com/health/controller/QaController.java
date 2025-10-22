package com.health.controller;

import com.health.common.Result;
import com.health.service.QaService;
    import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QaController {
    @Autowired
    private QaService qaService;

    // 问答查询接口：POST请求，参数question
    @PostMapping("/api/qa/query")
    public Result<List<String>> queryAnswer(@RequestParam String question) {
        // 参数校验（非空判断）
        if (question == null || question.trim().isEmpty()) {
            return Result.fail("请输入你的健康问题！");
        }
       List<String> answer = qaService.getAnswer(question);
        return Result.success(answer);
    }

}
