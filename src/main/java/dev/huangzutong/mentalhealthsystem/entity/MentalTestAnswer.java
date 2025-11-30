package dev.huangzutong.mentalhealthsystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 心理测试问卷作答表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mental_test_answer")
public class MentalTestAnswer{

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 作答学生对应用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 问卷ID
     */
    @TableField("test_id")
    private String testId;

    /**
     * 作答
     */
    @TableField("content")
    private String content;

    /**
     * 作答时间
     */
    @TableField("answer_time")
    private LocalDateTime answerTime;

    /**
     * 得分I（0-100）
     */
    @TableField("score")
    private Integer score;


}
