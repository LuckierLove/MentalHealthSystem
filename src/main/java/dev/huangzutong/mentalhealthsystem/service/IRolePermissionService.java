package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色权限关系表 服务类
 */
public interface IRolePermissionService extends IService<RolePermission> {

    /**
     * 根据角色ID获取权限列表
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<String> getPermissionByRoleId(String roleId);

    /**
     * 添加角色权限
     * @param roleId 角色ID
     * @param permissionList 权限列表
     */
    void addRolePermission(String roleId, List<String> permissionList);

    /**
     * 删除角色权限
     * @param roleId 角色ID
     * @param permission 权限列表
     */
    void deleteRolePermission(String roleId, List<String> permission);

    /**
     * 更新角色权限
     * @param roleId 角色ID
     * @param permission 权限列表
     */
    void updatePermission(String roleId, List<String> permission);
}
