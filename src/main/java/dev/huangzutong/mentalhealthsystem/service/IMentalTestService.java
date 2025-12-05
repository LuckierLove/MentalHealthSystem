package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.MentalTest;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.huangzutong.mentalhealthsystem.entity.req.AddMentalTestReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;

import java.util.List;

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

    /**
     * 获取心理健康测试问卷列表
     *
     * @param page       页码
     * @param pageSize   页大小
     * @param keyword    关键词
     * @param counselorId 咨询师ID
     * @return 心理健康测试问卷列表
     */
    GetListVO<List<MentalTest>> list(Long page, Long pageSize, String keyword, String counselorId);
}
