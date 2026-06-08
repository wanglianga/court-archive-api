package com.court.archive.common;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> handleRuntime(RuntimeException e) {
        return Result.error(400, e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result<?> handleValid(Exception e) {
        String msg;
        if (e instanceof MethodArgumentNotValidException) {
            msg = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).collect(Collectors.joining("; "));
        } else {
            msg = ((BindException) e).getBindingResult().getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).collect(Collectors.joining("; "));
        }
        return Result.error(400, "参数校验失败: " + msg);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> handleException(Exception e) {
        return Result.error(500, "系统异常: " + e.getMessage());
    }
}
