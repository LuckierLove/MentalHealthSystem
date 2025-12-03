package dev.huangzutong.mentalhealthsystem.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 添加咨询师请求参数
 */
@Data
public class AddCounselorReq {
    /**
     * 对应用户ID
     */
    @JsonProperty("user_id")
    private String userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 擅长领域
     */
    private String specialty;
    /**
     * 个人简介
     */
    private String introduction;
}
