package dev.huangzutong.mentalhealthsystem.entity.vo;

import lombok.Data;

/**
 * 树洞帖子返回
 */
@Data
public class GetTreeholePostVO {
    /**
     * 树洞帖子id
     */
    private String id;
    /**
     * 树洞帖子标题
     */
    private String title;
    /**
     * 树洞帖子内容
     */
    private String content;
    /**
     * 树洞帖子发布者用户id
     */
    private String userId;
    /**
     * 树洞帖子发布时间
     */
    private String postTime;
}
