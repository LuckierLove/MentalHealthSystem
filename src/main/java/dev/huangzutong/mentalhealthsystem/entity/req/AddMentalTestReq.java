package dev.huangzutong.mentalhealthsystem.entity.req;

import lombok.Data;

/**
 * 创建心理测试问卷请求参数
 */
@Data
public class AddMentalTestReq {
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 内容
     */
    private String content;
    /**
     * 阈值
     */
    private Integer threshold;
}
