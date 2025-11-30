package dev.huangzutong.mentalhealthsystem.controller;

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
     */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterReq req){
        log.info("用户注册：{}", req);
        userService.register(req);
        return Result.success();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<Void> login(@RequestBody LoginReq req){
        log.info("用户登录：{}", req);
        userService.login(req);
        return Result.success();
    }
}
