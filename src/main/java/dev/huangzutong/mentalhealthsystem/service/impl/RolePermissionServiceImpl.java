package dev.huangzutong.mentalhealthsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dev.huangzutong.mentalhealthsystem.entity.RolePermission;
import dev.huangzutong.mentalhealthsystem.mapper.RolePermissionMapper;
import dev.huangzutong.mentalhealthsystem.service.IRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色权限关系表 服务实现类
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {

    @Resource
    private RolePermissionMapper rolePermissionMapper;
    /**
     * 根据角色ID获取权限列表
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public List<String> getPermissionByRoleId(String roleId) {
        QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<RolePermission>()
                .eq("role_id", roleId);
        return rolePermissionMapper.selectList(rolePermissionQueryWrapper)
                .stream()
                .map(RolePermission::getPermission)
                .toList();
    }

    /**
     * 添加角色权限
     * @param roleId        角色ID
     * @param permissionList 权限列表
     */
    @Override
    public void addRolePermission(String roleId, List<String> permissionList) {
        List<RolePermission> rolePermissionList = permissionList.stream()
                .map(permission -> new RolePermission()
                        .setPermission(permission)
                        .setRoleId(roleId))
                .toList();
        rolePermissionMapper.insert(rolePermissionList);
    }
}
