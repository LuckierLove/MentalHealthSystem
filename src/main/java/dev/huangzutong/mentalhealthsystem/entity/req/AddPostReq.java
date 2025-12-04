package dev.huangzutong.mentalhealthsystem.entity.req;

import lombok.Data;

/**
 * 创建树洞帖子请求参数
 */
@Data
public class AddPostReq {
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
}
