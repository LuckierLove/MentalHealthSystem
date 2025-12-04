package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.huangzutong.mentalhealthsystem.entity.Appointment;
import dev.huangzutong.mentalhealthsystem.entity.req.CreateAppointmentReq;
import dev.huangzutong.mentalhealthsystem.mapper.AppointmentMapper;
import dev.huangzutong.mentalhealthsystem.service.IAppointmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * 预约表 服务实现类
 */
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService {

    /**
     * 添加预约
     * @param req 添加预约请求参数
     */
    @Override
    public void addAppointment(CreateAppointmentReq req) {
        String studentId = StpUtil.getLoginId().toString();
        Appointment appointment = Appointment.builder()
                .id(new SnowflakeGenerator().next())
                .counselorId(req.getCounselorId())
                .studentId(studentId)
                .appointmentTime(req.getAppointmentTime())
                .type(req.getType())
                .status(1)
                .rating(5)
                .build();
        save(appointment);
    }

    /**
     * 更新预约
     * @param id 预约id
     * @param req 更新预约请求参数
     */
    @Override
    public void updateAppointment(Long id, CreateAppointmentReq req) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        BeanUtils.copyProperties(req, appointment);
        updateById(appointment);
    }
}
