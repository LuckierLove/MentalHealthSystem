package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.req.AddPermissionReq;
import dev.huangzutong.mentalhealthsystem.service.IRolePermissionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 权限安全相关接口
 */
@Slf4j
@RequestMapping("/ps")
@RestController
@SaCheckLogin
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

    /**
     * 获取当前用户角色权限
     *
     * @return 当前用户角色权限
     */
    @GetMapping("/current")
    public Result<Map<String, List<String>>> getCurrentRole() {
        log.info("获取当前用户角色权限: {}", StpUtil.getLoginId());
        return Result.success(Map.of("permission", StpUtil.getPermissionList()));
    }

    /**
     * 添加角色权限
     *
     * @param role_id        角色ID
     * @param req 添加权限请求参数
     * @return 添加结果
     */
    @SaCheckPermission(value = "permission:add", orRole = "管理员")
    @PostMapping("/{role_id}")
    public Result<Void> addRolePermission(@PathVariable String role_id, @RequestBody AddPermissionReq req) {
        log.info("添加角色权限：{}", role_id);
        log.info("添加角色权限 当前用户权限: {}", StpUtil.getPermissionList());
        log.info("添加角色权限 当前用户角色: {}", StpUtil.getRoleList());
        rolePermissionService.addRolePermission(role_id, req.getPermission());
        return Result.success();
    }
}
