package dev.huangzutong.mentalhealthsystem.service.impl;

import dev.huangzutong.mentalhealthsystem.entity.MentalTest;
import dev.huangzutong.mentalhealthsystem.mapper.MentalTestMapper;
import dev.huangzutong.mentalhealthsystem.service.IMentalTestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 心理健康测试问卷表 服务实现类
 */
@Service
public class MentalTestServiceImpl extends ServiceImpl<MentalTestMapper, MentalTest> implements IMentalTestService {

}
