package com.poemSys.common.exception;

import com.poemSys.common.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常捕获
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice
{
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e)
    {
        log.error("Assert异常："+e.getMessage());
//        e.printStackTrace();
        return new Result(-1, e.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = AccessDeniedException.class)
    public Result handler(AccessDeniedException e)
    {
        log.error("security权限不足异常："+e.getMessage());
//        e.printStackTrace();
        return new Result(-2, e.getMessage(), null);
    }


    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Result handler(HttpMessageNotReadableException e)
    {
        log.error("请求数据格式不正确："+e.getMessage());
//        e.printStackTrace();
        return new Result(-3, "请求数据格式不正确", e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e)
    {
        log.error("运行时异常："+e.getMessage());
//        e.printStackTrace();
        return new Result(-1, e.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Result handler(HttpRequestMethodNotSupportedException e)
    {
        log.error("请求方法异常");
        return new Result(-1, "请求方法不支持", null);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = RedisConnectionFailureException.class)
    public Result handler(RedisConnectionFailureException e)
    {
        log.error("redis连接失败："+e.getMessage());
        return new Result(-1, "服务器异常，redis连接失败", e.getMessage());
    }


}
