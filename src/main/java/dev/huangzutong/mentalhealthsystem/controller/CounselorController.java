package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.req.AddCounselorReq;
import dev.huangzutong.mentalhealthsystem.service.ICounselorService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 咨询师相关接口
 */
@SaCheckLogin
@Slf4j
@RestController
@RequestMapping("/counselor")
public class CounselorController {
    @Resource
    private ICounselorService counselorService;

    /**
     * 添加咨询师
     *
     * @param req 添加咨询师请求参数
     * @return 添加结果
     */
    @PostMapping
    @SaCheckPermission(value = "counselor:add", orRole = "管理员")
    public Result<Void> addCounselor(@RequestBody AddCounselorReq req) {
        log.info("添加咨询师 {}", req);
        counselorService.addCounselor(req);
        return Result.success();
    }
}
