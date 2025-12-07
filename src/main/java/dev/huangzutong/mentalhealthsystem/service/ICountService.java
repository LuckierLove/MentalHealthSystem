package dev.huangzutong.mentalhealthsystem.service;

/**
 * 统计服务
 */
public interface ICountService {
    /**
     * 获取总预约数量
     *
     * @return 总预约数量
     */
    Long countTotalAppointment();

    /**
     * 获取今日预约数量
     *
     * @return 今日预约数量
     */
    Long countTotalAppointmentToday();

    /**
     * 获取咨询师数量
     *
     * @return 咨询师数量
     */
    Long countCounselor();

    /**
     * 获取用户数量
     *
     * @return 用户数量
     */
    Long countUser();
}
