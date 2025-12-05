package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.req.AddMentalTestReq;
import dev.huangzutong.mentalhealthsystem.service.IMentalTestService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 问卷相关接口
 */
@SaCheckLogin
@Slf4j
@RequestMapping("/mt")
@RestController
public class MentalTestController {
    @Resource
    private IMentalTestService mentalTestService;

    /**
     * 添加问卷
     * @param req 添加问卷请求参数
     * @return 添加结果
     */
    @SaCheckPermission(value = "mentalTest.add", orRole = "咨询师")
    @PostMapping
    public Result<Void> addMentalTest(@RequestBody AddMentalTestReq req) {
        log.info("添加问卷：{}", req);
        mentalTestService.addMentalTest(req);
        return Result.success();
    }
}
