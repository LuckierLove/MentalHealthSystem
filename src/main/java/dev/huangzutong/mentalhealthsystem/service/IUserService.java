package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.huangzutong.mentalhealthsystem.entity.req.CreateUserReq;
import dev.huangzutong.mentalhealthsystem.entity.req.LoginReq;
import dev.huangzutong.mentalhealthsystem.entity.req.RegisterReq;

/**
 * 基础用户表 服务类
 */
public interface IUserService extends IService<User> {

    /**
     * 注册
     * @param req 注册请求参数
     */
    void register(RegisterReq req);

    /**
     * 登录
     * @param req 登录请求参数
     */
    void login(LoginReq req);

    /**
     * 创建用户
     * @param req 创建用户请求参数
     */
    void createUser(CreateUserReq req);

    /**
     * 删除用户
     * @param userId 用户ID
     */
    void delete(Long userId);
}
