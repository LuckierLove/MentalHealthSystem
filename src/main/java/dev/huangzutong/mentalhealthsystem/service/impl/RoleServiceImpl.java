package dev.huangzutong.mentalhealthsystem.service.impl;

import dev.huangzutong.mentalhealthsystem.entity.Role;
import dev.huangzutong.mentalhealthsystem.mapper.RoleMapper;
import dev.huangzutong.mentalhealthsystem.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 角色表 服务实现类
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
