package dev.huangzutong.mentalhealthsystem.common;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.SaTokenException;
import com.alibaba.fastjson2.JSONObject;
import dev.huangzutong.mentalhealthsystem.common.constant.UserMessageConstant;
import dev.huangzutong.mentalhealthsystem.common.exception.BaseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public String handleBaseException(BaseException e) {
        return JSONObject.toJSONString(Result.failed(e.getMessage()));
    }

    @ExceptionHandler(NotLoginException.class)
    public String handleNotLoginException(NotLoginException e) {
        return JSONObject.toJSONString(Result.failed(UserMessageConstant.NOT_LOGIN));
    }

    @ExceptionHandler(SaTokenException.class)
    public String handleSaTokenException(SaTokenException e) {
        e.printStackTrace();
        return JSONObject.toJSONString(Result.failed(e.getMessage()));
    }
}
