package dev.huangzutong.mentalhealthsystem.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 咨询师表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("counselor")
public class Counselor{

    /**
     * 咨询师ID
     */
    @TableId(value = "id", type = IdType.NONE)
    private String id;

    /**
     * 对应用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 擅长领域
     */
    @TableField("specialty")
    private String specialty;

    /**
     * 个人简介
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 评分（保留一位小数）
     */
    @TableField("rating")
    private BigDecimal rating;


}
