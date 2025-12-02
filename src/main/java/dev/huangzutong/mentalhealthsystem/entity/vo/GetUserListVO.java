package dev.huangzutong.mentalhealthsystem.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * 获取用户列表返回
 */
@Data
public class GetUserListVO {
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 当前页码
     */
    private Long page;
    /**
     * 记录数
     */
    private List<GetUserInfoVO> records;
}
