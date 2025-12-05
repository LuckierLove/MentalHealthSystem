package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.Role;
import dev.huangzutong.mentalhealthsystem.service.IRoleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色相关接口
 */
@Slf4j
@RestController
@RequestMapping("/role")
@SaCheckLogin
@SaCheckRole("管理员")
public class RoleController {
    @Resource
    private IRoleService roleService;
    /**
     * 创建角色
     * @param role 创建角色参数
     * @return  创建结果
     */
    @PostMapping
    public Result<Void> createRole(@RequestBody Role role) {
        log.info("创建角色 {}", role);
        roleService.save(role.setId(new SnowflakeGenerator().next().toString()));
        return Result.success();
    }
}
