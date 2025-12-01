package dev.huangzutong.mentalhealthsystem.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 创建用户请求参数
 */
@Data
public class CreateUserReq {
    /**
     * 用户名
     * 长度8-20
     */
    private String username;
    /**
     * 密码
     * 8-20
     */
    private String password;
    /**
     * 昵称
     * 长度3-16
     */
    private String nickname;
    /**
     * 真实姓名
     * 长度2-5
     */
    @JsonProperty("real_name")
    private String realName;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像URL
     */
    private String avatar;
    /**
     * 角色ID
     */
    @JsonProperty("role_id")
    private String roleId;
}
