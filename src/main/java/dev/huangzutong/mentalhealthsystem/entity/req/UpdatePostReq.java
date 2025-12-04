package dev.huangzutong.mentalhealthsystem.entity.req;

import lombok.Data;

/**
 * 更新树洞帖子请求参数
 */
@Data
public class UpdatePostReq {
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 审核状态
     */
    private Integer pass;
}
