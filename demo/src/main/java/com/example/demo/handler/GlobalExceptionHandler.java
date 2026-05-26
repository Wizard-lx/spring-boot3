package com.example.demo.handler;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotPermissionException;
import com.example.demo.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
//统一异常拦截类
@RestControllerAdvice
public class GlobalExceptionHandler {
    //没有权限拦截方法
    @ExceptionHandler(NotPermissionException.class)
    public Result noPermissionException(NotPermissionException e){
        SaHolder.getResponse().setStatus(403);
        return Result.error(e.getMessage());
    }
}
