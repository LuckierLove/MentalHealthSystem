package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.Appointment;
import dev.huangzutong.mentalhealthsystem.entity.req.CreateAppointmentReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.service.IAppointmentService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预约相关接口
 */
@SaCheckLogin
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

    /**
     * 获取预约详情
     * @param id 预约id
     * @return 预约详情
     */
    @GetMapping("/{id}")
    public Result<Appointment> getAppointment(@PathVariable Long id) {
        log.info("获取预约详情 {}", id);
        return Result.success(appointmentService.getById(id));
    }

    /**
     * 更新预约
     * @param id 预约id
     * @param req 更新预约请求参数
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<Void> updateAppointment(@PathVariable Long id, @RequestBody Appointment req) {
        log.info("更新预约 {}", req);
        appointmentService.updateAppointment(id, req);
        return Result.success();
    }

    /**
     * 获取预约列表
     * @param page 页码
     * @param pageSize 页大小
     * @param studentId 学生对应用户ID
     * @param counselorId 咨询师对应用户ID
     * @return 预约列表
     */
    @GetMapping("/list")
    public Result<GetListVO<List<Appointment>>> listAppointment(@RequestParam(required = false, defaultValue = "1") Long page,
                                                                @RequestParam(required = false, defaultValue = "10") Long pageSize,
                                                                @RequestParam(required = false) String studentId,
                                                                @RequestParam(required = false) String counselorId) {
        return Result.success(appointmentService.listAppointment(page, pageSize, studentId, counselorId));
    }
}
