package dev.huangzutong.mentalhealthsystem.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 获取用户信息返回展示类
 */
@Data
public class GetUserInfoVO {
    /**
     * 用户ID
     */
    private String id;
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
    /**
     * 角色名
     */
    private String role;
}
