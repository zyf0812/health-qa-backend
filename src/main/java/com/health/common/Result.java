package com.health.common;

import lombok.Data;

@Data
//泛型T,支持返回任意类型数据
public class Result<T> {
    private Integer code; // 状态码 200 = 成功 ，400 = 失败
    private String message; // 提示的信息
    private T data; // 返回的数据

    // 成功时调用 (返回数据)
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    // 失败时调用（返回提示信息）
    public static <T> Result<T> fail(String msg) {
        Result<T> result = new Result<>();
        result.setCode(400);
        result.setMessage(msg);
        return result;
    }


}
