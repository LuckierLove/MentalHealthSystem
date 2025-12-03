package dev.huangzutong.mentalhealthsystem.entity.req;

import lombok.Data;

import java.util.List;

/**
 * 添加权限请求参数
 */
@Data
public class AddPermissionReq {
    /**
     * 权限列表
     */
    private List<String> permission;
}
