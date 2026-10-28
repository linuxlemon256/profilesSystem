package com.example.profilessystem.exception;

import com.example.profilessystem.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionGlobalHandler {
    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e) {
        return Result.fail("some thing is wrong");
    }
}
