package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.huangzutong.mentalhealthsystem.entity.MentalTest;
import dev.huangzutong.mentalhealthsystem.entity.req.AddMentalTestReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.mapper.MentalTestMapper;
import dev.huangzutong.mentalhealthsystem.service.IMentalTestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 心理健康测试问卷表 服务实现类
 */
@Service
public class MentalTestServiceImpl extends ServiceImpl<MentalTestMapper, MentalTest> implements IMentalTestService {
    @Resource
    private MentalTestMapper mentalTestMapper;

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

    /**
     * 获取心理健康测试问卷列表
     *
     * @param page       页码
     * @param pageSize   页大小
     * @param keyword    关键词
     * @param counselorId 咨询师ID
     * @return 心理健康测试问卷列表
     */
    @Override
    public GetListVO<List<MentalTest>> list(Long page, Long pageSize, String keyword, String counselorId) {
        Page<MentalTest> pageHelper = new Page<>(page, pageSize);
        QueryWrapper<MentalTest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(counselorId), "user_id", counselorId);
        queryWrapper.like(StringUtils.isNotBlank(keyword), "title", keyword);
        mentalTestMapper.selectPage(pageHelper, queryWrapper);
        return new GetListVO<>(pageHelper.getTotal(), pageHelper.getCurrent(), pageHelper.getRecords());
    }
}
