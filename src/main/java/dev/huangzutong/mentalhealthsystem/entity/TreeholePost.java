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

/*
 * 树洞帖子表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("treehole_post")
public class TreeholePost{

    /**
     * 帖子ID
     */
    @TableId(value = "id", type = IdType.NONE)
    private String id;

    /**
     * 帖子标题
     */
    @TableField("title")
    private String title;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 发帖人用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 发帖时间
     */
    @TableField("post_time")
    private LocalDateTime postTime;

    /**
     * 审核是否通过（0：不通过； 1：通过）
     */
    @TableField("pass")
    private Integer pass;


}
