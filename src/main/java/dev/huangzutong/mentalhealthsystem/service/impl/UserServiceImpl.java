package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dev.huangzutong.mentalhealthsystem.common.constant.UserMessageConstant;
import dev.huangzutong.mentalhealthsystem.common.enums.UserRoleEnum;
import dev.huangzutong.mentalhealthsystem.common.exception.UserException;
import dev.huangzutong.mentalhealthsystem.entity.User;
import dev.huangzutong.mentalhealthsystem.entity.UserRole;
import dev.huangzutong.mentalhealthsystem.entity.req.CreateUserReq;
import dev.huangzutong.mentalhealthsystem.entity.req.LoginReq;
import dev.huangzutong.mentalhealthsystem.entity.req.RegisterReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetUserInfoVO;
import dev.huangzutong.mentalhealthsystem.mapper.RoleMapper;
import dev.huangzutong.mentalhealthsystem.mapper.UserMapper;
import dev.huangzutong.mentalhealthsystem.mapper.UserRoleMapper;
import dev.huangzutong.mentalhealthsystem.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基础用户表 服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMapper roleMapper;

    /**
     * 注册
     * @param req 注册请求参数
     */
    @Transactional
    @Override
    public void register(RegisterReq req) {
        // 校验参数
        validateRegisterReq(req);
        User user = new User()
                .setId(new SnowflakeGenerator().next().toString())
                .setUsername(req.getUsername())
                .setPassword(new BCryptPasswordEncoder().encode(req.getPassword()))
                .setNickname(req.getNickname())
                .setRealName(req.getRealName())
                .setEmail(req.getEmail())
                .setAvatar(req.getAvatar());
        // 保存用户
        save(user);

        // 保存用户角色
        userRoleMapper.insert(new UserRole()
                .setUserId(user.getId())
                .setRoleId(UserRoleEnum.STUDENT.getId())
        );
    }

    /**
     * 登录
     * @param req 登录请求参数
     */
    @Override
    public void login(LoginReq req) {
        User user = getOne(new QueryWrapper<User>().eq("username", req.getUsername()));
        // 获取用户信息
        if(user == null){
            throw new UserException(UserMessageConstant.USER_NOT_EXIST);
        }

        // 密码校验
        if(!new BCryptPasswordEncoder().matches(req.getPassword(), user.getPassword())){
            throw new UserException(UserMessageConstant.PASSWORD_ERROR);
        }

        // 登录
        StpUtil.login(user.getId());
    }

    /**
     * 创建用户
     * @param req 创建用户请求参数
     */
    @Transactional
    @Override
    public void createUser(CreateUserReq req) {
        RegisterReq registerReq = new RegisterReq();
        BeanUtils.copyProperties(req, registerReq);
        validateRegisterReq(registerReq);
        User user = new User()
                .setId(new SnowflakeGenerator().next().toString())
                .setUsername(req.getUsername())
                .setPassword(new BCryptPasswordEncoder().encode(req.getPassword()))
                .setNickname(req.getNickname())
                .setRealName(req.getRealName())
                .setEmail(req.getEmail())
                .setAvatar(req.getAvatar());
        // 保存用户
        save(user);

        // 保存用户角色
        userRoleMapper.insert(new UserRole()
                .setUserId(user.getId())
                .setRoleId(UserRoleEnum.STUDENT.getId())
        );
    }

    /**
     * 删除用户
     * @param userId 用户id
     */
    @Transactional
    @Override
    public void delete(String userId) {
        removeById(userId);
        userRoleMapper.delete(new QueryWrapper<UserRole>().eq("user_id", userId));
    }

    /**
     * 获取用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    @Override
    public GetUserInfoVO getUserInfo(String userId) {
        GetUserInfoVO userInfo = new GetUserInfoVO();
        BeanUtils.copyProperties(getById(userId), userInfo);
        String roleId = userRoleMapper.selectOne(new QueryWrapper<UserRole>().eq("user_id", userId)).getRoleId();
        String roleName = roleMapper.selectById(roleId).getName();
        userInfo.setRole(roleName);
        return userInfo;
    }

    /**
     * 校验注册请求参数
     * @param req 注册请求参数
     */
    private void validateRegisterReq(RegisterReq req) {
        // 判断用户名长度是否为8-20位
        if (req.getUsername().length() < 8 || req.getUsername().length() > 20) {
            throw new UserException(UserMessageConstant.USERNAME_LENGTH_ERROR);
        }

        // 密码长度是否为8-20位
        if (req.getPassword().length() < 8 || req.getPassword().length() > 20) {
            throw new UserException(UserMessageConstant.PASSWORD_LENGTH_ERROR);
        }

        // 昵称长度是否为2-20位
        if (req.getNickname().length() < 2 || req.getNickname().length() > 20) {
            throw new UserException(UserMessageConstant.NICKNAME_LENGTH_ERROR);
        }

        // 判断真实姓名长度是否为2-5位
        if (req.getRealName().length() < 2 || req.getRealName().length() > 5) {
            throw new UserException(UserMessageConstant.REAL_NAME_LENGTH_ERROR);
        }

        // 邮箱格式是否正确
        if (!Validator.isEmail(req.getEmail())) {
            throw new UserException(UserMessageConstant.EMAIL_FORMAT_ERROR);
        }
    }
}
