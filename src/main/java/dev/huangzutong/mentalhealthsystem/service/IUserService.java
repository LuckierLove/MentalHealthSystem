package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.huangzutong.mentalhealthsystem.entity.req.CreateUserReq;
import dev.huangzutong.mentalhealthsystem.entity.req.LoginReq;
import dev.huangzutong.mentalhealthsystem.entity.req.RegisterReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetUserInfoVO;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetUserListVO;

import java.util.List;

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
    void delete(String userId);

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    GetUserInfoVO getUserInfo(String userId);

    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param req 更新用户信息请求参数
     */
    void updateUser(String userId, CreateUserReq req);

    /**
     * 获取用户列表
     * @param page 页码
     * @param pageSize 页大小
     * @param keyword 昵称关键词
     * @return 用户列表
     */
    GetListVO<List<GetUserInfoVO>> listUser(Long page, Long pageSize, String keyword);
}
