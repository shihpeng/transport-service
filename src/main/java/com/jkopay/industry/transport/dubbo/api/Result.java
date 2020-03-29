package com.jkopay.industry.transport.dubbo.api;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private T data;
    private boolean success;
    private String message;

    public static <T> Result<T> failedResult(String message) {
        Result<T> failed = new Result<T>();
        failed.success = false;
        failed.message = message;
        return failed;
    }

    public static <T> Result<T> success(T data) {
        Result<T> success = new Result<T>();
        success.data = data;
        success.success = true;
        return success;
    }

    public static <T> Result<T> success() {
        Result<T> success = new Result<T>();
        success.success = true;
        return success;
    }
}
