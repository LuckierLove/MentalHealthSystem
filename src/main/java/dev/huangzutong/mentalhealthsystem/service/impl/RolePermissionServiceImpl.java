package dev.huangzutong.mentalhealthsystem.service.impl;

import dev.huangzutong.mentalhealthsystem.entity.RolePermission;
import dev.huangzutong.mentalhealthsystem.mapper.RolePermissionMapper;
import dev.huangzutong.mentalhealthsystem.service.IRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 角色权限关系表 服务实现类
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {

}
