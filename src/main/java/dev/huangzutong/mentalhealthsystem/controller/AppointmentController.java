package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.req.CreateAppointmentReq;
import dev.huangzutong.mentalhealthsystem.service.IAppointmentService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
     * @param req 创建预约请求参数
     * @return 创建结果
     */
    @RequestMapping
    public Result<Void> addAppointment(@RequestBody CreateAppointmentReq req) {
        log.info("创建预约 {}",  req);
        appointmentService.addAppointment(req);
        return Result.success();
    }

    /**
     * 取消预约
     * @param id 预约id
     * @return 取消结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteAppointment(@PathVariable Long id) {
        log.info("删除预约 {}", id);
        appointmentService.removeById(id);
        return Result.success();
    }
}
