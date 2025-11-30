package dev.huangzutong.mentalhealthsystem.service.impl;

import dev.huangzutong.mentalhealthsystem.entity.User;
import dev.huangzutong.mentalhealthsystem.mapper.UserMapper;
import dev.huangzutong.mentalhealthsystem.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 基础用户表 服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
