package dev.huangzutong.mentalhealthsystem.common;

import com.alibaba.fastjson2.JSONObject;
import dev.huangzutong.mentalhealthsystem.common.exception.BaseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public String handleBaseException(BaseException e) {
        e.printStackTrace();
        return JSONObject.toJSONString(Result.failed(e.getMessage()));
    }
}
