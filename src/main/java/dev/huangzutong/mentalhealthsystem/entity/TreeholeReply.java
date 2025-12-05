package dev.huangzutong.mentalhealthsystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 树洞帖子回复表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("treehole_reply")
public class TreeholeReply{
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 帖子ID
     */
    @TableField("post_id")
    @JsonProperty("post_id")
    private String postId;

    /**
     * 发送回复的用户ID
     */
    @TableField("user_id")
    @JsonProperty("user_id")
    private String userId;

    /**
     * 回复内容
     */
    @TableField("content")
    private String content;

    /**
     * 回复时间
     */
    @TableField("reply_time")
    @JsonProperty("reply_time")
    private LocalDateTime replyTime;


}
