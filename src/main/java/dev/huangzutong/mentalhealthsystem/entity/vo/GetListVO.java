package dev.huangzutong.mentalhealthsystem.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 通用列表返回
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class GetListVO<T> {
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 当前
     */
    private Long page;
    /**
     * 数据记录
     */
    private T records;
}
