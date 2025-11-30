package dev.huangzutong.mentalhealthsystem.service.impl;

import dev.huangzutong.mentalhealthsystem.entity.Appointment;
import dev.huangzutong.mentalhealthsystem.mapper.AppointmentMapper;
import dev.huangzutong.mentalhealthsystem.service.IAppointmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 预约表 服务实现类
 */
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService {

}
