package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dev.huangzutong.mentalhealthsystem.entity.RolePermission;
import dev.huangzutong.mentalhealthsystem.entity.UserRole;
import dev.huangzutong.mentalhealthsystem.service.IRolePermissionService;
import dev.huangzutong.mentalhealthsystem.service.IRoleService;
import dev.huangzutong.mentalhealthsystem.service.IUserRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Sa-Token 自定义权限校验类
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    @Resource
    private IUserRoleService userRoleService;
    @Resource
    private IRolePermissionService rolePermissionService;
    @Resource
    private IRoleService roleService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        UserRole userRole = userRoleService.getOne(new QueryWrapper<UserRole>().eq("user_id", loginId));
        return rolePermissionService.list(new QueryWrapper<RolePermission>().eq("role_id", userRole.getRoleId()))
                .stream()
                .map(RolePermission::getPermission)
                .toList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        UserRole userRole = userRoleService.getOne(new QueryWrapper<UserRole>().eq("user_id", loginId));
        return List.of(roleService.getOptById(userRole.getRoleId()).get().getName());
    }
}
