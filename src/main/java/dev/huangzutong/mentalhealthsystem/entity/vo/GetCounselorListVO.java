package dev.huangzutong.mentalhealthsystem.entity.vo;

import dev.huangzutong.mentalhealthsystem.entity.Counselor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 获取咨询师列表返回
 */
@Data
@Accessors(chain = true)
public class GetCounselorListVO {
    /**
     * 当前页码
     */
    private Long page;
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 记录
     */
    private List<Counselor> records;
}
