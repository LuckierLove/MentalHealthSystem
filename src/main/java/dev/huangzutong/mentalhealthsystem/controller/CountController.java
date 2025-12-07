package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.service.ICountService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计接口
 */
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/count")
public class CountController {
    @Resource
    private ICountService countService;
    /**
     * 获取总预约数量
     *
     * @return 总预约数量
     */
    @GetMapping("/appointment")
    public Result<Long> getAppointmentCount() {
        log.info("获取总预约数量");
        return Result.success(countService.countTotalAppointment());
    }

    /**
     * 获取今日预约数量
     *
     * @return 今日预约数量
     */
    @GetMapping("/appointment/today")
    public Result<Long> getAppointmentCountToday() {
        log.info("获取今日预约数量");
        return Result.success(countService.countTotalAppointmentToday());
    }

    /**
     * 获取咨询师数量
     *
     * @return 咨询师数量
     */
    @GetMapping("/counselor")
    public Result<Long> getCounselorCount() {
        log.info("获取咨询师数量");
        return Result.success(countService.countCounselor());
    }

    /**
     * 获取用户数量
     *
     * @return 用户数量
     */
    @GetMapping("/user")
    public Result<Long> getUserCount() {
        log.info("获取用户数量");
        return Result.success(countService.countUser());
    }
}
