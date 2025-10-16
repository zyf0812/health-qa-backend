package com.health.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

// 扩大扫描范围到整个 com.health 根包
@SpringBootApplication(scanBasePackages = "com.health")
// 显式扫描 Repository 包（双重保障）
@EnableJdbcRepositories(basePackages = "com.health.repository")
public class DemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
    }

}
