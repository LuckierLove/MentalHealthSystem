package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.MentalTest;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.huangzutong.mentalhealthsystem.entity.req.AddMentalTestReq;

/**
 * 心理健康测试问卷表 服务类
 */
public interface IMentalTestService extends IService<MentalTest> {

    /**
     * 添加心理健康测试问卷
     *
     * @param req 添加心理健康测试问卷参数
     */
    void addMentalTest(AddMentalTestReq req);
}
