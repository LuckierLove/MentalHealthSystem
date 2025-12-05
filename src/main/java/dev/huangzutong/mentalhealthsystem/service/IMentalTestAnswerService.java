package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.MentalTestAnswer;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;

import java.util.List;

/**
 * 心理测试问卷作答表 服务类
 */
public interface IMentalTestAnswerService extends IService<MentalTestAnswer> {

    /**
     * 创建心理测试问卷作答
     *
     * @param req 创建心理测试问卷作答参数
     */
    void createAnswer(MentalTestAnswer req);

    /**
     * 获取心理测试问卷作答列表
     *
     * @param page       页码
     * @param pageSize   页大小
     * @param testId     心理测试问卷ID
     * @param userId     作答用户ID
     * @return 心理测试问卷作答列表
     */
    GetListVO<List<MentalTestAnswer>> list(Long page, Long pageSize, String testId, String userId);
}
