package com.health.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // 标识这是接口类，返回JSON格式数据
public class TestController {
    // 接口路径：/api/hello，GET请求
    @GetMapping("/api/hello")
    public String sayHello() {
        return "健康养生问答系统后端启动成功！";
    }
}
