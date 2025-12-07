package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.huangzutong.mentalhealthsystem.entity.Appointment;
import dev.huangzutong.mentalhealthsystem.entity.Counselor;
import dev.huangzutong.mentalhealthsystem.entity.req.CreateAppointmentReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.mapper.AppointmentMapper;
import dev.huangzutong.mentalhealthsystem.mapper.CounselorMapper;
import dev.huangzutong.mentalhealthsystem.service.IAppointmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 预约表 服务实现类
 */
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService {

    @Resource
    private AppointmentMapper appointmentMapper;
    @Resource
    private CounselorMapper counselorMapper;

    /**
     * 添加预约
     * @param req 添加预约请求参数
     */
    @Override
    public void addAppointment(CreateAppointmentReq req) {
        String studentId = StpUtil.getLoginId().toString();
        Appointment appointment = Appointment.builder()
                .counselorId(req.getCounselorId())
                .studentId(studentId)
                .appointmentTime(req.getAppointmentTime())
                .type(req.getType())
                .status(1)
                .rating(0)
                .build();
        save(appointment);
    }

    /**
     * 更新预约
     * @param id 预约id
     * @param appointment 更新预约请求参数
     */
    @Transactional
    @Override
    public void updateAppointment(Long id, Appointment appointment) {
        appointment.setId(id);
        updateById(appointment);

        // 更新对应咨询师评分
        if(appointment.getRating() != null){
            Counselor counselor = counselorMapper.selectOne(new QueryWrapper<Counselor>().eq("user_id", appointmentMapper.selectById(id).getCounselorId()));
            counselor.setRating((counselor.getRating().add(BigDecimal.valueOf(appointment.getRating())).divide(BigDecimal.valueOf(2))));
            counselorMapper.updateById(counselor);
        }
    }

    /**
     * 获取预约列表
     * @param page 页码
     * @param pageSize 页大小
     * @param studentId 学生ID
     * @param counselorId 咨询师ID
     * @return 预约列表
     */
    @Override
    public GetListVO<List<Appointment>> listAppointment(Long page, Long pageSize, String studentId, String counselorId) {
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(studentId), "student_id", studentId);
        queryWrapper.eq(StringUtils.isNotBlank(counselorId), "counselor_id", counselorId);
        Page<Appointment> pageHelper = new Page<>(page, pageSize);
        appointmentMapper.selectPage(pageHelper, queryWrapper);
        return new GetListVO<>(pageHelper.getTotal(), pageHelper.getCurrent(), pageHelper.getRecords());
    }
}
