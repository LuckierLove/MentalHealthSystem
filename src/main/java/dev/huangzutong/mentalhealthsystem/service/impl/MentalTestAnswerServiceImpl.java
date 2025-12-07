package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.huangzutong.mentalhealthsystem.entity.MentalTest;
import dev.huangzutong.mentalhealthsystem.entity.MentalTestAnswer;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.mapper.MentalTestAnswerMapper;
import dev.huangzutong.mentalhealthsystem.mapper.MentalTestMapper;
import dev.huangzutong.mentalhealthsystem.service.IMentalTestAnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 心理测试问卷作答表 服务实现类
 */
@Service
public class MentalTestAnswerServiceImpl extends ServiceImpl<MentalTestAnswerMapper, MentalTestAnswer> implements IMentalTestAnswerService {
    @Resource
    private MentalTestAnswerMapper mentalTestAnswerMapper;
    @Resource
    private MentalTestMapper mentalTestMapper;
    /**
     * 创建心理测试问卷作答
     * @param req 创建心理测试问卷作答参数
     */
    @Override
    public void createAnswer(MentalTestAnswer req) {
        req.setUserId(StpUtil.getLoginId().toString());
        req.setAnswerTime(LocalDateTime.now());
        req.setScore(getScore(req.getTestId(), req.getContent()));
        save(req);
    }

    /**
     * 获取心理测试问卷作答列表
     *
     * @param page       页码
     * @param pageSize   页大小
     * @param testId     心理测试问卷ID
     * @param userId     作答用户ID
     * @return 心理测试问卷作答列表
     */
    @Override
    public GetListVO<List<MentalTestAnswer>> list(Long page, Long pageSize, String testId, String userId) {
        QueryWrapper<MentalTestAnswer> queryWrapper = new QueryWrapper<>();
        Page<MentalTestAnswer> pageHelper = new Page<>(page, pageSize);
        queryWrapper.eq(StringUtils.isNotBlank(testId), "test_id", testId);
        queryWrapper.eq(StringUtils.isNotBlank(userId), "user_id", userId);
        mentalTestAnswerMapper.selectPage(pageHelper, queryWrapper);
        return new GetListVO<>(pageHelper.getTotal(), pageHelper.getCurrent(), pageHelper.getRecords());
    }

    /**
     * 更新心理测试问卷作答
     *
     * @param mentalTestAnswer 心理测试问卷作答
     * @param id 心理测试问卷作答ID
     */
    @Override
    public void update(Long id, MentalTestAnswer mentalTestAnswer) {
        String testId = getById(id).getTestId();
        mentalTestAnswer.setScore(getScore(testId, mentalTestAnswer.getContent()));
        mentalTestAnswer.setAnswerTime(LocalDateTime.now());
        mentalTestAnswer.setId(id);
        updateById(mentalTestAnswer);
    }

    /**
     * 获取心理测试问卷作答分数
     * @param testId 心理测试问卷id
     * @param answer 作答内容
     * @return 作答分数
     */
    public Integer getScore(String testId, String answer) {
        JSONArray answers = JSONArray.parse(answer);

        Integer score = 0;
        for (int i = 0; i < answers.size(); i++) {
            JSONObject as = answers.getJSONObject(i);
            Integer choose = as.getInteger("choose");
            // choose 是选项的分值，直接加到总分中
            score += choose;
        }
        return score;
    }


}
