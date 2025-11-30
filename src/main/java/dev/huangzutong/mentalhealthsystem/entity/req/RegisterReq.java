package dev.huangzutong.mentalhealthsystem.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 注册请求参数
 */
@Data
public class RegisterReq {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 真实姓名
     */
    @JsonProperty("real_name")
    private String realName;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像
     */
    private String avatar;
}
