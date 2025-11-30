package dev.huangzutong.mentalhealthsystem.common;

import dev.huangzutong.mentalhealthsystem.common.constant.ResultCode;
import lombok.Data;

/**
 * 统一返回结果类
 * @param <T> 返回数据类型
 */
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    /**
     * 成功返回结果
     * @param data 数据
     * @return Result
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * 成功返回结果
     * @return Result
     */
    public static <T> Result<T> success(){
        return success(null);
    }

    /**
     * 失败返回结果
     * @param data 数据
     * @return Result
     */
    public static <T> Result<T> failed(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.FAILED);
        result.setMessage("failed");
        result.setData(data);
        return result;
    }

    /**
     * 失败返回结果
     * @return Result
     */
    public static <T> Result<T> failed(){
        return failed(null);
    }
}
