package dev.huangzutong.mentalhealthsystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 预约表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("appointment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment{

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 咨询师对应用户ID
     */
    @TableField("counselor_id")
    private String counselorId;

    /**
     * 学生对应用户ID
     */
    @TableField("student_id")
    private String studentId;

    /**
     * 预约时间
     */
    @TableField("appointment_time")
    private LocalDateTime appointmentTime;

    /**
     * 预约类型（1：线上；2：线下）
     */
    @TableField("type")
    private Integer type;

    /**
     * 状态（1：待确认；2：未完成；3：已完成；4：爽约）
     */
    @TableField("status")
    private Integer status;

    /**
     * 评分（1-5）
     */
    @TableField("rating")
    private Integer rating;


}
