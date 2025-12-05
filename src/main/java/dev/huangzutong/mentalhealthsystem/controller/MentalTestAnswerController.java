package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.MentalTestAnswer;
import dev.huangzutong.mentalhealthsystem.service.IMentalTestAnswerService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 心理测试作答相关接口
 */
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/mta")
public class MentalTestAnswerController {
    @Resource
    private IMentalTestAnswerService mentalTestAnswerService;

    /**
     * 创建心理测试问卷作答
     *
     * @param req 创建心理测试问卷作答参数
     * @return 创建结果
     */
    @RequestMapping
    public Result<Void> createAnswer(@RequestBody MentalTestAnswer req){
        log.info("创建心理测试作答：{}", req);
        mentalTestAnswerService.createAnswer(req);
        return Result.success();
    }
}
