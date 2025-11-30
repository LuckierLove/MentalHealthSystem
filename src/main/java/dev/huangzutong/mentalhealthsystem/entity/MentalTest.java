package dev.huangzutong.mentalhealthsystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 心理健康测试问卷表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mental_test")
public class MentalTest{

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.NONE)
    private String id;

    /**
     * 发布问卷咨询师对应用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 问卷标题
     */
    @TableField("title")
    private String title;

    /**
     * 问卷描述
     */
    @TableField("description")
    private String description;

    /**
     * 问卷内容
     */
    @TableField("content")
    private String content;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 警告阈值
     */
    @TableField("threshold")
    private Integer threshold;


}
