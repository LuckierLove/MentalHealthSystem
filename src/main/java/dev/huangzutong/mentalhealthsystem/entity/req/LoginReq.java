package dev.huangzutong.mentalhealthsystem.entity.req;

import lombok.Data;

/**
 * 登录请求参数
 */
@Data
public class LoginReq {
    private String username;
    private String password;
}
