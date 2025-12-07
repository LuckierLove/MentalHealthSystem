package dev.huangzutong.mentalhealthsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dev.huangzutong.mentalhealthsystem.entity.Appointment;
import dev.huangzutong.mentalhealthsystem.mapper.AppointmentMapper;
import dev.huangzutong.mentalhealthsystem.mapper.CounselorMapper;
import dev.huangzutong.mentalhealthsystem.mapper.UserMapper;
import dev.huangzutong.mentalhealthsystem.service.ICountService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 统计服务实现类
 */
@Service
public class CountServiceImpl implements ICountService {
    @Resource
    private AppointmentMapper appointmentMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CounselorMapper counselorMapper;
    /**
     * 获取总预约数量
     *
     * @return 总预约数量
     */
    @Override
    public Long countTotalAppointment() {
        return appointmentMapper.selectCount( null);
    }

    /**
     * 获取今日预约数量
     *
     * @return 今日预约数量
     */
    @Override
    public Long countTotalAppointmentToday() {
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("appointment_time", LocalDateTime.now().withHour(0).withMinute(0).withSecond(0));
        return appointmentMapper.selectCount(queryWrapper);
    }

    /**
     * 获取咨询师数量
     *
     * @return 咨询师数量
     */
    @Override
    public Long countCounselor() {
        return counselorMapper.selectCount(null);
    }

    /**
     * 获取用户数量
     *
     * @return 用户数量
     */
    @Override
    public Long countUser() {
        return userMapper.selectCount( null);
    }
}
