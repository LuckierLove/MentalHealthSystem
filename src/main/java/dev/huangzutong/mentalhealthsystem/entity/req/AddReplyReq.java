package dev.huangzutong.mentalhealthsystem.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 创建树洞帖子回复请求参数
 */
@Data
public class AddReplyReq {
    /**
     * 帖子id
     */
    @JsonProperty("post_id")
    private String postId;
    /**
     * 回复内容
     */
    private String content;
}
