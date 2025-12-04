package dev.huangzutong.mentalhealthsystem.entity.req;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 创建咨询预约请求参数
 */
@Data
public class CreateAppointmentReq {
    /**
     * 咨询师对应用户ID
     */
    @JsonProperty("counselor_id")
    private String counselorId;
    /**
     * 咨询时间
     */
    @JsonProperty("appointment_time")
    private LocalDateTime appointmentTime;
    /**
     * 咨询类型
     * 1：线上；2：线下
     */
    private Integer type;
}
