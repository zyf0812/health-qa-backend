package com.health.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 处理运行时异常
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return Result.fail("服务器内部错误：" + e.getMessage());
    }

    // 处理IO异常（如爬虫超时、文件操作失败）
    @ExceptionHandler(IOException.class)
    public Result<Void> handleIOException(IOException e) {
        e.printStackTrace();
        return Result.fail("IO操作异常：" + e.getMessage());
    }

    // 处理所有未捕获的异常（兜底）
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        e.printStackTrace();
        return Result.fail("系统异常：" + e.getMessage());
    }
}