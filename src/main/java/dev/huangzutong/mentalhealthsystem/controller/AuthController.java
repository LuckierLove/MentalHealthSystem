package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.stp.StpUtil;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.req.LoginReq;
import dev.huangzutong.mentalhealthsystem.entity.req.RegisterReq;
import dev.huangzutong.mentalhealthsystem.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 鉴权相关接口
 */
@RequestMapping("/auth")
@RestController
@Slf4j
public class AuthController {
    @Resource
    private IUserService userService;
    /**
     * 注册
     * @param req 注册参数
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterReq req){
        log.info("用户注册：{}", req);
        userService.register(req);
        return Result.success();
    }

    /**
     * 登录
     * @param req 登录参数
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginReq req){
        log.info("用户登录：{}", req);
        userService.login(req);
        return Result.success(Map.of("token", StpUtil.getTokenInfo()));
    }
}
