package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.req.CreateUserReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetUserInfoVO;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetUserListVO;
import dev.huangzutong.mentalhealthsystem.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 用户相关接口
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;
    /**
     * 创建用户
     * @param req 创建用户请求
     * @return 创建结果
     */
    @PostMapping
    @SaCheckPermission(value = "user:create", orRole = "管理员")
    public Result<Void> createUser(@RequestBody CreateUserReq req) {
        log.info("创建用户 {}",  req);
        userService.createUser(req);
        return Result.success();
    }

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{user_id}")
    @SaCheckPermission(value = "user:delete", orRole = "管理员")
    public Result<Void> deleteUser(@PathVariable("user_id") String userId) {
        log.info("删除用户 {}", userId);
        log.info("当前用户权限 {}", StpUtil.getPermissionList());
        userService.delete(userId);
        return Result.success();
    }

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/{user_id}")
    public Result<GetUserInfoVO> getUserInfo(@PathVariable("user_id") String userId) {
        log.info("获取用户信息 {}", userId);
        GetUserInfoVO userInfo = userService.getUserInfo(userId);
        return Result.success(userInfo);
    }

    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param req 更新用户信息请求
     * @return 更新结果
     */
    @PutMapping("/{user_id}")
    public Result<Void> updateUser(@PathVariable("user_id") String userId, @RequestBody CreateUserReq req) {
        log.info("更新用户 {}", userId);
        userService.updateUser(userId, req);
        return Result.success();
    }

    /**
     * 获取用户列表
     * @param page 页码
     * @param pageSize 页大小
     * @param keyword 昵称关键词
     * @return 用户列表
     */
    @GetMapping("/list")
    @SaCheckPermission(value = "user:list", orRole = "管理员")
    public Result<GetUserListVO> listUser(@RequestParam(required = false, defaultValue = "1") Long page,
                                          @RequestParam(required = false, defaultValue = "10")Long pageSize,
                                          @RequestParam(required = false) String keyword) {
        log.info("获取用户列表 当前用户权限: {}", StpUtil.getPermissionList());
        log.info("获取用户列表 当前用户角色: {}", StpUtil.getRoleList());
        return Result.success(userService.listUser(page, pageSize, keyword));
    }
}
