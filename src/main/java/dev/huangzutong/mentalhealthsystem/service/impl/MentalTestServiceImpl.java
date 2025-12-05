package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.huangzutong.mentalhealthsystem.entity.MentalTest;
import dev.huangzutong.mentalhealthsystem.entity.req.AddMentalTestReq;
import dev.huangzutong.mentalhealthsystem.mapper.MentalTestMapper;
import dev.huangzutong.mentalhealthsystem.service.IMentalTestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 心理健康测试问卷表 服务实现类
 */
@Service
public class MentalTestServiceImpl extends ServiceImpl<MentalTestMapper, MentalTest> implements IMentalTestService {

    /**
     * 添加心理健康测试问卷
     *
     * @param req 添加心理健康测试问卷请求参数
     */
    @Override
    public void addMentalTest(AddMentalTestReq req) {
        MentalTest mentalTest = new MentalTest();
        BeanUtils.copyProperties(req, mentalTest);
        mentalTest.setId(new SnowflakeGenerator().next().toString());
        mentalTest.setUserId(StpUtil.getLoginId().toString());
        mentalTest.setCreateTime(LocalDateTime.now());
        save(mentalTest);
    }
}
