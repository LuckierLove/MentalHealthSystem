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
 * 对话记录
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dialog")
public class Dialog {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 咨询师ID
     */
    @TableField("counselor_id")
    private String counselorId;

    /**
     * 对话记录
     */
    @TableField("content")
    private String content;

    /**
     * 对话时间
     */
    @TableField("dialog_time")
    private LocalDateTime dialogTime;


}
