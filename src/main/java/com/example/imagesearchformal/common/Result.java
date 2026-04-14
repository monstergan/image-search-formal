package com.example.imagesearchformal.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private boolean success;
    private String code;
    private String message;
    private T data;

    public static <T> Result<T> ok(T data) {
        return new Result<T>(true, "200", "success", data);
    }

    public static <T> Result<T> ok(String message) {
        return new Result<T>(true, "200", message, null);
    }

    public static <T> Result<T> fail(String code, String message) {
        return new Result<T>(false, code, message, null);
    }
}
