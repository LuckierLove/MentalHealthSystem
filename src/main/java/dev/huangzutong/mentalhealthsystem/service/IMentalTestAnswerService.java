package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.MentalTestAnswer;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
