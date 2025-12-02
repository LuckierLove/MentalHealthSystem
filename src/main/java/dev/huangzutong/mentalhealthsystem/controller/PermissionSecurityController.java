package dev.huangzutong.mentalhealthsystem.controller;

import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.service.IRolePermissionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 权限安全相关接口
 */
@Slf4j
@RequestMapping("/ps")
@RestController
public class PermissionSecurityController {
    @Resource
    private IRolePermissionService rolePermissionService;
    /**
     * 获取角色权限
     *
     * @param role_id 角色ID
     * @return 角色权限
     */
    @GetMapping("/{role_id}")
    public Result<Map<String, List<String>>> getRole(@PathVariable String role_id) {
        log.info("获取角色权限：{}", role_id);
        List<String> permissionList = rolePermissionService.getPermissionByRoleId(role_id);
        return Result.success(Map.of("permission", permissionList));
    }
}
