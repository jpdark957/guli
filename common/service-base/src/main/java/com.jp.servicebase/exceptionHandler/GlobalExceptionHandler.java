package com.jp.servicebase.exceptionHandler;

import com.jp.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //全局异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理类..");
    }

    //ArithmeticException异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R error(ArithmeticException e){
        log.error(ExceptionUtil.getMessage(e));
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException异常处理类..");
    }

    //自定义异常
    @ExceptionHandler(JpExceptionHandler.class)
    @ResponseBody
    public R error(JpExceptionHandler e){
        log.error(ExceptionUtil.getMessage(e));
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
