package com.health.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 拦截所有@RestController注解的类的异常
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 处理所有RuntimeException（运行时异常）
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        // 打印异常信息（便于调试）
        e.printStackTrace();
        // 返回失败提示
        return Result.fail("服务器内部错误：" + e.getMessage());
    }

    // 可添加其他异常处理（如SQL异常、参数异常等）
}
