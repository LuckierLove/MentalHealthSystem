package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.Appointment;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.huangzutong.mentalhealthsystem.entity.req.CreateAppointmentReq;

/**
 * 预约表 服务类
 */
public interface IAppointmentService extends IService<Appointment> {

    /**
     * 添加预约
     *
     * @param req 添加预约请求参数
     */
    void addAppointment(CreateAppointmentReq req);
}
