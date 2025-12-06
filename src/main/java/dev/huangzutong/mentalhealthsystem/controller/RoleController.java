package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.Role;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.service.IRoleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 更新角色
     * @param id 角色ID
     * @param role 更新角色参数
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<Void> updateRole(@PathVariable String id, @RequestBody Role role) {
        log.info("更新角色 {}", role);
        roleService.updateById(role.setId(id));
        return Result.success();
    }

    /**
     * 删除角色
     * @param id 角色ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable String id) {
        log.info("删除角色 {}", id);
        roleService.removeById(id);
        return Result.success();
    }

    /**
     * 获取角色列表
     * @param page 页码
     * @param pageSize 页大小
     * @param keyword 角色名关键字
     * @return 角色列表
     */
    @GetMapping("/list")
    public Result<GetListVO<List<Role>>> getRoleList(@RequestParam(defaultValue = "1") Long page,
                                                     @RequestParam(defaultValue = "10") Long pageSize,
                                                     @RequestParam(required = false) String keyword) {
        log.info("获取角色列表 {} {} {}", page, pageSize, keyword);
        return Result.success(roleService.list(page, pageSize,  keyword));
    }
}
