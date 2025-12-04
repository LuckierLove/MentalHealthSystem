package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.req.CreateAppointmentReq;
import dev.huangzutong.mentalhealthsystem.service.IAppointmentService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 预约相关接口
 */
@SaCheckPermission
@RestController
@RequestMapping("/appointment")
@Slf4j
public class AppointmentController {
    @Resource
    private IAppointmentService appointmentService;
    /**
     * 创建预约
     */
    @RequestMapping
    public Result<Void> addAppointment(@RequestBody CreateAppointmentReq req) {
        log.info("创建预约 {}",  req);
        appointmentService.addAppointment(req);
        return Result.success();
    }
}
