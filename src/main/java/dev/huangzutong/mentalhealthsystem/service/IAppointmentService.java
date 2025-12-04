package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.Appointment;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.huangzutong.mentalhealthsystem.entity.req.CreateAppointmentReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;

import java.util.List;

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

    /**
     * 修改预约
     *
     * @param id  预约id
     * @param req 修改预约请求参数
     */
    void updateAppointment(Long id, CreateAppointmentReq req);

    /**
     * 获取预约列表
     *
     * @param page         页码
     * @param pageSize     页大小
     * @param studentId    学生id
     * @param counselorId  咨询师id
     * @return 预约列表
     */
    GetListVO<List<Appointment>> listAppointment(Long page, Long pageSize, String studentId, String counselorId);
}
